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
import com.aashreys.walls.Utils;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.adapters.ImageStreamAdapter;
import com.aashreys.walls.ui.tasks.LoadImagesTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class ImageStreamFragment extends Fragment implements ImageStreamAdapter.LoadMoreCallback,
        LoadImagesTask.ImageLoadCallback {

    private static final String TAG = ImageStreamFragment.class.getSimpleName();

    private static final String ARG_COLLECTION = "arg_collection";

    private static final String SAVED_IMAGE_LIST = "saved_image_list";

    private static final String SAVED_COLLECTION = "saved_collection";

    @Inject Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy;

    @Inject SourceFactory sourceFactory;

    @Nullable private FavoriteImageRepository favoriteImageRepository;

    @Nullable private RepositoryCallback<Image> favoriteRepoListener;

    private Collection argCollection;

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
    public ImageStreamFragment() {
    }

    public static ImageStreamFragment newInstance(Collection collection) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_COLLECTION, collection);
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
        if (context instanceof ImageSelectedCallback) {
            this.imageSelectedListener = (ImageSelectedCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageSelectedListener and ImageSourceProvider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_COLLECTION) || args.getParcelable
                (ARG_COLLECTION) == null) {
            throw new RuntimeException("Must pass a collection as an argument.");
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

        setCollection((Collection) getArguments().getParcelable(ARG_COLLECTION), false);

        if (savedInstanceState != null) {
            // We check if the saved collection and the collection passed via args are the same. If
            // yes, load saved image list else discard saved data and load from network. This
            // check is done so that we can properly respond to the user adding new collections
            // which may change the underlying collection associated with this fragment.
            // See ImageStreamViewPagerAdapter.java for more information.
            Collection savedCollection = savedInstanceState.getParcelable(SAVED_COLLECTION);
            List<Image> imageList = savedInstanceState.getParcelableArrayList(SAVED_IMAGE_LIST);
            //noinspection ConstantConditions
            boolean isSavedStateValid = argCollection.equals(savedCollection) && imageList != null
                    && imageList.size() > 0;
            if (isSavedStateValid) {
                Log.d(TAG, argCollection.name().value() + " - loading images from saved state");
                adapter.add(imageList);
            } else {
                loadImagesFromNetwork();
            }
        } else {
            loadImagesFromNetwork();
        }

        // Start listening to scrolling load requests after we have queued the initial data to load
        adapter.setLoadMoreCallback(this);

        return view;
    }

    private void setupRecyclerView() {
        int columnCount = Utils.getStreamColumnCount(getContext());
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

    public void setCollection(Collection collection, boolean loadImages) {
        if (argCollection == null || !argCollection.equals(collection)) {
            argCollection = collection;
            imageSource = sourceFactory.create(collection);
            adapter.clear();
            if (collection instanceof FavoriteCollection) {
                // Remove bottom padding since favorites load instantly and we don't need to show
                // the extra space to the user
                setRecyclerViewBottomPaddingEnabled(false);
                startListeningToFavoritesRepo();
            } else {
                // Add bottom padding since we load images over network and it might take some
                // time. When the images load the user will see the empty space fill up and know
                // more images are available.
                setRecyclerViewBottomPaddingEnabled(true);
                stopListeningToFavoritesRepo();
            }
            if (loadImages) {
                loadImagesFromNetwork();
            }
        }
    }

    private void loadImagesFromNetwork() {
        Log.d(TAG, argCollection.name().value() + " - loading images from network");
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

    private void setRecyclerViewBottomPaddingEnabled(boolean isEnabled) {
        if (recyclerView != null) {
            int bottomPadding = getResources().getDimensionPixelOffset(isEnabled ? R.dimen
                    .image_stream_bottom_padding : R.dimen.image_margin);
            recyclerView.setPadding(
                    recyclerView.getPaddingLeft(),
                    recyclerView.getPaddingTop(),
                    recyclerView.getPaddingRight(),
                    bottomPadding
            );
        }
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.getItemCount() > 0) {
            outState.putParcelableArrayList(
                    SAVED_IMAGE_LIST,
                    (ArrayList<Image>) adapter.getImageList()
            );
        }
        outState.putParcelable(SAVED_COLLECTION, argCollection);
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
        Log.d(TAG, "load more callback called");
        if (loadImagesTask == null || !loadImagesTask.isLoading()) {
            loadImagesFromNetwork();
        }
    }

    @Override
    public void onImageLoadComplete(@NonNull List<Image> images) {
        if (images.size() > 0) {
            boolean isFirstLoad = adapter.getItemCount() == 0;
            adapter.add(images);
            if (isFirstLoad) {
                recyclerView.scrollToPosition(0);
            }
        } else {
            // TODO: Display error message of some sort
        }
    }

    public interface ImageSelectedCallback {

        void onImageSelected(Image image);

    }

}
