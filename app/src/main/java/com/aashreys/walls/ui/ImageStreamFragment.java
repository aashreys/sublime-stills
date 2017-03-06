package com.aashreys.walls.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.adapters.ImageStreamAdapter;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.tasks.LoadImagesTask;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class ImageStreamFragment extends Fragment implements ImageStreamAdapter.LoadMoreCallback,
        LoadImagesTask.ImageLoadCallback {

    private static final String TAG = ImageStreamFragment.class.getSimpleName();

    private static final String ARG_POSITION = "arg_position";

    @Inject Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy;

    @Inject SourceFactory sourceFactory;

    @Nullable private FavoriteImageRepository favoriteImageRepository;

    @Nullable private RepositoryCallback<Image> favoriteRepoListener;

    private CollectionProvider collectionProvider;

    private Collection collection;

    private Source imageSource;

    private ImageStreamAdapter adapter;

    private RecyclerView recyclerView;

    private LoadImagesTask loadImagesTask;

    private ImageSelectedCallback imageSelectedListener;

    private boolean isDisplayed;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageStreamFragment() {}

    public static ImageStreamFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        ImageStreamFragment fragment = new ImageStreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        isDisplayed = visible;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((WallsApplication) context.getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
        if (context instanceof ImageSelectedCallback && context instanceof CollectionProvider) {
            this.imageSelectedListener = (ImageSelectedCallback) context;
            this.collectionProvider = (CollectionProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageSelectedListener and CollectionProvider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getArguments().containsKey(ARG_POSITION)) {
            throw new RuntimeException("Must pass position as an argument.");
        }
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_image_stream, container, false);
        recyclerView = (RecyclerView) view;
        setupRecyclerView();
        adapter = new ImageStreamAdapter(this, imageSelectedListener);
        recyclerView.setAdapter(adapter);
        setCollection(collectionProvider.getCollection(getArguments().getInt(ARG_POSITION)));
        // Start listening to scrolling load requests after we have queued the initial data to load
        adapter.setLoadMoreCallback(this);

        return view;
    }

    private void setupRecyclerView() {
        int columnCount = UiHelper.getStreamColumnCount(getContext());
        if (columnCount == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager
                    = new StaggeredGridLayoutManager(
                    columnCount,
                    StaggeredGridLayoutManager.VERTICAL
            );
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager
                    .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
    }

    /**
     * Setter for this fragment's collection. This fragment expects its collection to be set
     * using this method only as there are many actions which must be taken after a collection is
     * set. This method contains code to take all associated actions.
     */
    public void setCollection(Collection collection) {
        if (!collection.equals(this.collection)) {
            this.collection = collection;
            imageSource = sourceFactory.create(collection);
            adapter.clear();
            if (collection instanceof FavoriteCollection) {
                startListeningToFavoritesRepo();
            } else {
                stopListeningToFavoritesRepo();
            }
            loadImages();
        }
    }

    private void loadImages() {
        Log.i(TAG, collection.getName().value() + " - loading images");
        if (loadImagesTask != null) {
            loadImagesTask.release();
            loadImagesTask = null;
        }
        loadImagesTask = new LoadImagesTask(imageSource, this);
        loadImagesTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                adapter.getItemCount()
        );
    }

    private void startListeningToFavoritesRepo() {
        favoriteImageRepository = favoriteImageRepositoryLazy.get();
        favoriteRepoListener = new RepositoryCallback<Image>() {
            @Override
            public void onInsert(Image image) {
                if (adapter != null) {
                    adapter.addFavorite(image);
                }
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(0);
                }
            }

            @Override
            public void onUpdate(Image object) {

            }

            @Override
            public void onDelete(Image image) {
                if (adapter != null) {
                    adapter.remove(image);
                }
            }
        };
        //noinspection ConstantConditions
        favoriteImageRepository.addListener(favoriteRepoListener);
    }

    private void stopListeningToFavoritesRepo() {
        if (favoriteImageRepository != null && favoriteRepoListener != null) {
            favoriteImageRepository.removeListener(favoriteRepoListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    private void releaseResources() {
        stopListeningToFavoritesRepo();
        if (loadImagesTask != null) {
            loadImagesTask.release();
            loadImagesTask = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        imageSelectedListener = null;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    @Override
    public void onLoadMore() {
        if (loadImagesTask == null || !loadImagesTask.isLoading()) {
            loadImages();
        }
    }

    @Override
    public void onImageLoadComplete(@NonNull List<Image> images) {
        adapter.onLoadComplete();
        if (images.size() > 0) {
            adapter.add(images);
        } else {
            // TODO: Display error message of some sort
        }
    }

    public interface ImageSelectedCallback {

        void onImageSelected(Image image);

    }

    public interface CollectionProvider {

        Collection getCollection(int position);

    }

}
