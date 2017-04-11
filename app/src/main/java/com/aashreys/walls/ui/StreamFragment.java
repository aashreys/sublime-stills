/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aashreys.walls.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
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
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.adapters.StreamAdapter;
import com.aashreys.walls.ui.helpers.NetworkHelper;
import com.aashreys.walls.ui.tasks.LoadImagesTask;
import com.aashreys.walls.ui.views.LoadingView;
import com.aashreys.walls.ui.views.StreamImageView;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.END_OF_COLLECTION;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.FAVORITE;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.GENERIC_ERROR;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.LOADING;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.NOT_LOADING;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.NO_INTERNET;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.SLOW_INTERNET;

public class StreamFragment extends Fragment implements StreamAdapter.LoadingCallback,
        LoadImagesTask.LoadCallback {

    private static final String TAG = StreamFragment.class.getSimpleName();

    private static final String ARG_POSITION = "arg_position";

    @Inject Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy;

    @Inject DeviceInfo deviceInfo;

    @Inject SourceFactory sourceFactory;

    @Nullable private FavoriteImageRepository favoriteImageRepository;

    @Nullable private RepositoryCallback<Image> favoriteRepoListener;

    private StreamActivity.CollectionProvider collectionProvider;

    private Collection collection;

    private Source imageSource;

    private StreamAdapter adapter;

    private RecyclerView recyclerView;

    private LoadImagesTask loadImagesTask;

    private LoadingViewStateManager loadingViewStateManager;

    private LoadingView loadingView;

    private StreamImageView.InteractionCallback imageSelectedListener;

    private boolean isDisplayed;

    private boolean isFavoritesStream;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StreamFragment() {}

    public static StreamFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        StreamFragment fragment = new StreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((WallsApplication) context.getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
        if (context instanceof StreamActivity) {
            StreamActivity activity = (StreamActivity) context;
            this.imageSelectedListener = activity.getImageInteractionCallback();
            this.collectionProvider = activity.getCollectionProvider();
            this.collection = collectionProvider.getCollection(getArguments().getInt(ARG_POSITION));
            this.imageSource = sourceFactory.create(collection);
            isFavoritesStream = collection instanceof FavoriteCollection;
        } else {
            throw new RuntimeException(
                    context.toString() + " must be an instance of StreamActivity");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getArguments().containsKey(ARG_POSITION)) {
            throw new RuntimeException("Must pass position as an argument.");
        }
        loadingViewStateManager = new LoadingViewStateManager();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        setupRecyclerView(view);
        setupLoadingView();
        adapter = new StreamAdapter(this, imageSelectedListener);
        recyclerView.setAdapter(adapter);
        adapter.setLoadingView(loadingView);
        adapter.setLoadingCallback(this);
        if (isFavoritesStream) {
            startListeningToFavoritesRepo();
        } else {
            stopListeningToFavoritesRepo();
        }
        loadImages();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        imageSelectedListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        isDisplayed = visible;
    }


    private void setupLoadingView() {
        this.loadingView = (LoadingView) LayoutInflater.from(getContext()).inflate(
                R.layout.view_loading,
                recyclerView,
                false
        );
        loadingViewStateManager.loadingView = this.loadingView;
    }

    private void setupRecyclerView(View parentView) {
        this.recyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerview);
        int columnCount = deviceInfo.getNumberOfStreamColumns();
        RecyclerView.LayoutManager manager = null;
        if (columnCount == 1) {
            manager = new LinearLayoutManager(getContext());
            ((LinearLayoutManager) manager).setInitialPrefetchItemCount(5);
        } else {
            manager = new StaggeredGridLayoutManager(
                    columnCount,
                    StaggeredGridLayoutManager.VERTICAL
            );

            ((StaggeredGridLayoutManager) manager).setGapStrategy(StaggeredGridLayoutManager
                    .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }
        manager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(manager);
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
                adapter.getImageCount()
        );
        if (!isFavoritesStream) {
            Context context = getContext();
            if (context != null) {
                if (!NetworkHelper.isConnected(context)) {
                    loadingViewStateManager.setState(NO_INTERNET);
                } else if (!NetworkHelper.isFastNetworkConnected(context)) {
                    loadingViewStateManager.setState(SLOW_INTERNET);
                } else {
                    loadingViewStateManager.setState(LOADING);
                }
            } else {
                loadingViewStateManager.setState(LOADING);
            }
        } else {
            loadingViewStateManager.setState(FAVORITE);
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

    private void releaseResources() {
        stopListeningToFavoritesRepo();
        if (loadImagesTask != null) {
            loadImagesTask.release();
            loadImagesTask = null;
        }
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    @Override
    public void onLoadRequested() {
        if (loadImagesTask == null || !loadImagesTask.isLoading()) {
            loadImages();
        }
    }

    @Override
    public void onLoadComplete(@NonNull List<Image> images) {
        adapter.add(images);
        if (isFavoritesStream) {
            loadingViewStateManager.setState(FAVORITE);
        } else {
            if (images.size() > 0) {
                loadingViewStateManager.setState(NOT_LOADING);
            } else {
                loadingViewStateManager.setState(END_OF_COLLECTION);
            }
        }
    }

    @Override
    public void onLoadError() {
        loadingViewStateManager.setState(GENERIC_ERROR);
    }

    static class LoadingViewStateManager {

        private int state;

        private LoadingView loadingView;

        private StreamAdapter.LoadingCallback loadingCallback;

        void setState(@State int state) {
            if (this.state != state) {
                this.state = state;
                TransitionManager.beginDelayedTransition(loadingView, new AutoTransition());
                resetLoadingViewState();
                switch (state) {
                    case LOADING:
                        showLoadingState();
                        break;

                    case GENERIC_ERROR:
                        showGenericErrorState();
                        break;

                    case NO_INTERNET:
                        showNoInternetErrorState();
                        break;

                    case SLOW_INTERNET:
                        showSlowInternetState();
                        break;

                    case END_OF_COLLECTION:
                        showEndOfCollectionState();
                        break;

                    case FAVORITE:
                        showFavoriteState();
                        break;

                    case NOT_LOADING:
                        showNotLoadingState();
                }
            }
        }

        private void showNotLoadingState() {
            resetLoadingViewState();
            loadingView.hide();
        }

        private void showLoadingState() {
            loadingView.showProgressBar();
        }

        private void showGenericErrorState() {
            loadingView.setText(R.string.error_generic);
            loadingView.showText();

            loadingView.setIcon(R.drawable.ic_info_outline_black_24dp);
            loadingView.showIcon();

            loadingView.setActionButtonText(R.string.action_try_again);
            loadingView.setActionButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingCallback.onLoadRequested();
                }
            });
            loadingView.showActionButton();
        }

        private void showNoInternetErrorState() {
            loadingView.setText(R.string.error_no_connectivity);
            loadingView.showText();

            loadingView.setIcon(R.drawable.ic_info_outline_black_24dp);
            loadingView.showIcon();

            loadingView.setActionButtonText(R.string.action_try_again);
            loadingView.setActionButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingCallback.onLoadRequested();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingView.showActionButton();
                }
            }, 7000);
        }

        private void showSlowInternetState() {
            loadingView.setText(R.string.error_slow_connectivity);
            loadingView.showText();

            loadingView.showProgressBar();

            loadingView.setActionButtonText(R.string.action_try_again);
            loadingView.setActionButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingCallback.onLoadRequested();
                }
            });

            loadingView.showActionButton();
        }

        private void showFavoriteState() {
            loadingView.setText(R.string.hint_add_favorites);
            loadingView.showText();

            loadingView.setIcon(R.drawable.ic_favorite_black_24dp);
            loadingView.showIcon();
        }

        private void showEndOfCollectionState() {
            loadingView.setText(R.string.hint_end_of_collection);
            loadingView.showText();

            loadingView.setIcon(R.drawable.ic_info_outline_black_24dp);
            loadingView.showIcon();
        }

        private void resetLoadingViewState() {
            loadingView.hideProgressBar();
            loadingView.hideIcon();
            loadingView.hideText();
            loadingView.hideActionButton();
            loadingView.setActionButtonOnClickListener(null);
            loadingView.show();
        }

        @interface State {

            int LOADING = 0;

            int GENERIC_ERROR = 1;

            int NO_INTERNET = 2;

            int SLOW_INTERNET = 3;

            int END_OF_COLLECTION = 4;

            int FAVORITE = 5;

            int NOT_LOADING = 6;

        }

    }

}
