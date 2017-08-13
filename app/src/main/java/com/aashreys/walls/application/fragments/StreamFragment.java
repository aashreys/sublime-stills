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

package com.aashreys.walls.application.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.application.activities.StreamActivity;
import com.aashreys.walls.application.adapters.StreamAdapter;
import com.aashreys.walls.application.helpers.UiHelper;
import com.aashreys.walls.application.views.LoadingView;
import com.aashreys.walls.utils.LogWrapper;

import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.END_OF_COLLECTION;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.FAVORITE;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.GENERIC_ERROR;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.LOADING;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.NOT_LOADING;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.NO_INTERNET;
import static com.aashreys.walls.application.fragments.StreamFragment.LoadingViewStateManager.State.SLOW_INTERNET;

public class StreamFragment extends Fragment implements StreamFragmentModel.EventListener {

    private static final String TAG = StreamFragment.class.getSimpleName();

    private static final String ARG_POSITION = "arg_position";

    private StreamAdapter adapter;

    private RecyclerView recyclerView;

    private LoadingView loadingView;

    private LoadingViewStateManager loadingViewStateManager;

    private StreamFragmentModel viewModel;

    private boolean tempIsDisplayed;

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

    private void createViewModel() {
        viewModel = UiHelper.getUiComponent(getContext()).createStreamFragmentModel();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof StreamActivity && getArguments().containsKey(ARG_POSITION)) {
            createViewModel();
            viewModel.setIsDisplayed(tempIsDisplayed);
            StreamActivity activity = (StreamActivity) getActivity();
            viewModel.setCollection(activity.getCollectionProvider()
                    .getCollection(getArguments().getInt(ARG_POSITION)));
            viewModel.setImageInteractionListener(activity.getImageInteractionCallback());
            viewModel.setEventListener(this);
            viewModel.setStreamScrollListener(activity.getStreamScrollListener());
            loadingViewStateManager = new LoadingViewStateManager();
            setupRecyclerView(getView());
            setupLoadingView();
            setupAdapter();
            recyclerView.setAdapter(adapter);
            viewModel.onFragmentReady();
        } else {
            throw new RuntimeException("Parent must be an instance of StreamActivity");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onFragmentViewDestroyed();
    }

    private void setupAdapter() {
        adapter = new StreamAdapter(this, viewModel.getImageInteractionListener(), viewModel);
        adapter.setLoadingView(loadingView);
        adapter.setLoadingCallback(viewModel);
        adapter.setIsLoadingEnabled(true);
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        tempIsDisplayed = visible;
        if (viewModel != null) {
            viewModel.setIsDisplayed(visible);
        }
    }

    private void setupLoadingView() {
        this.loadingView = (LoadingView) LayoutInflater.from(getContext()).inflate(
                R.layout.view_loading,
                recyclerView,
                false
        );
        loadingViewStateManager.setLoadingView(loadingView);
        loadingViewStateManager.setLoadingCallback(viewModel);

    }

    private void setupRecyclerView(View parentView) {
        this.recyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerview);
        int columnCount = viewModel.getNumberOfStreamColumns();
        final RecyclerView.LayoutManager manager;
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogWrapper.d(TAG, "Y-axis scroll: " + dy);
                if (dy > 0) {
                    viewModel.notifyStreamScrollUp();
                } else {
                    viewModel.notifyStreamScrollDown();
                }
            }
        });
    }

    public boolean isDisplayed() {
        return viewModel != null ? viewModel.isDisplayed() : tempIsDisplayed;
    }

    @Override
    public void onImagesAdded(int positionStart, int itemCount) {
        if (adapter != null) {
            if (positionStart > 0) {
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onImageAdded(int position) {
        if (adapter != null) {
            adapter.notifyItemInserted(position);
        }
    }

    @Override
    public void onImageRemoved(int position) {
        if (adapter != null) {
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onImagesLoading(boolean isFavorite) {
        loadingViewStateManager.setState(isFavorite ? FAVORITE : LOADING);
    }

    @Override
    public void onNoNetworkError() {
        loadingViewStateManager.setState(NO_INTERNET);
    }

    @Override
    public void onSlowNetworkError() {
        loadingViewStateManager.setState(SLOW_INTERNET);
    }

    @Override
    public void onImageLoadingComplete(boolean isFavorite) {
        loadingViewStateManager.setState(isFavorite ? FAVORITE : NOT_LOADING);
    }

    @Override
    public void onCollectionEndReached(boolean isFavorite) {
        loadingViewStateManager.setState(isFavorite ? FAVORITE : END_OF_COLLECTION);
        adapter.setIsLoadingEnabled(false);
    }

    @Override
    public void onGenericError() {
        loadingViewStateManager.setState(GENERIC_ERROR);
    }

    static class LoadingViewStateManager {

        private int state;

        private LoadingView loadingView;

        private StreamAdapter.LoadingCallback loadingCallback;

        void setLoadingView(LoadingView loadingView) {
            this.loadingView = loadingView;
        }

        void setLoadingCallback(StreamAdapter.LoadingCallback loadingCallback) {
            this.loadingCallback = loadingCallback;
        }

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
