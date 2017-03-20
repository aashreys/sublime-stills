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

package com.aashreys.walls.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.ui.ImageStreamFragment;
import com.aashreys.walls.ui.views.LoadingView;
import com.aashreys.walls.ui.views.LoadingView.ViewMode;
import com.aashreys.walls.ui.views.StreamImageView;

import java.util.ArrayList;
import java.util.List;

import static com.aashreys.walls.ui.views.StreamImageView.ImageSelectedCallback;

/**
 * Created by aashreys on 04/02/17.
 */

public class ImageStreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ImageStreamAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_IMAGE = 0, VIEW_TYPE_LOADING = 1;

    @NonNull
    private final ImageStreamFragment fragment;

    @NonNull
    private final ImageSelectedCallback imageSelectedCallback;

    @NonNull
    private final List<Image> imageList;

    /**
     * To store the current loading view mode. We can't store this directly in the
     * {@link LoadingViewHolder} since we don't have access when this adapter is created, hence
     * storing it in the adapter. This value is delivered via
     * {@link LoadingView#setViewMode(int)} when the {@link LoadingViewHolder} is bound in
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} and via the
     * {@link #setLoadingState(int)}.
     */
    @ViewMode
    private int loadingViewMode;

    /**
     * How many items from the end of the list should the adapter request for more data to be
     * loaded via an instance of {@link LoadingCallback} set via
     * {@link #setLoadingCallback(LoadingCallback)}
     */
    private int loadingThreshold = 5;

    private LoadingCallback loadingCallback;

    @Nullable
    private LoadingViewHolder loadingViewHolder;

    public ImageStreamAdapter(
            @NonNull ImageStreamFragment fragment,
            @NonNull ImageSelectedCallback listener
    ) {
        this.fragment = fragment;
        this.imageSelectedCallback = listener;
        this.imageList = new ArrayList<>();
    }

    public void add(List<Image> images) {
        if (images != null && images.size() > 0) {
            int oldSize = imageList.size();
            imageList.addAll(images);
            if (oldSize != 0) {
                notifyItemRangeInserted(oldSize, images.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    // Special method for adding favorites since they are added to
    // the top of the list as opposed to the bottom like the other images.
    public void addFavorite(Image favoriteImage) {
        imageList.add(0, favoriteImage);
        notifyItemInserted(0);
    }

    public void add(Image image) {
        int oldSize = imageList.size();
        imageList.add(image);
        notifyItemInserted(oldSize);
    }

    public void remove(Image image) {
        int position = imageList.indexOf(image);
        if (position != -1) {
            imageList.remove(image);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        imageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                return new ImageViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.view_stream_image, parent, false)
                );

            case VIEW_TYPE_LOADING:
                loadingViewHolder = new LoadingViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.view_loading, parent, false)
                );
                return loadingViewHolder;

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(
                    imageList.get(position),
                    fragment,
                    imageSelectedCallback
            );
        }
        int currentPosition = holder.getAdapterPosition();
        int thresholdDiff = getItemCount() - currentPosition;
        if (loadingThreshold >= thresholdDiff && loadingCallback != null) {
            loadingCallback.onLoadRequested();
            if (holder instanceof LoadingViewHolder) {
                if (loadingViewHolder != null) {
                    loadingViewHolder.loadingView.setLoadingCallback(loadingCallback);
                    loadingViewHolder.loadingView.setViewMode(this.loadingViewMode);
                }
            }
        }
    }

    public int getImageCount() {
        // Removing 1 to discount the loading view
        return getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        // Adding one to count the loading view.
        return imageList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 1) {
            return VIEW_TYPE_IMAGE;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    /**
     * Set how many items before the end of the image list should this adapter request a new load.
     * See {@link ImageStreamAdapter#loadingThreshold}.
     */
    public void setLoadingThreshold(int lastNItems) {
        this.loadingThreshold = lastNItems;
    }

    public void setLoadingCallback(LoadingCallback loadingCallback) {
        this.loadingCallback = loadingCallback;
        if (loadingViewHolder != null) {
            loadingViewHolder.loadingView.setLoadingCallback(loadingCallback);
        }
    }

    /**
     * Tells the adapter which loading state it should display.
     *
     * @param mode integer value from {@link LoadingView.ViewMode}
     */
    public void setLoadingState(@ViewMode int mode) {
        this.loadingViewMode = mode;
        if (loadingViewHolder != null) {
            loadingViewHolder.loadingView.setViewMode(this.loadingViewMode);
            if (loadingCallback != null) {
                loadingViewHolder.loadingView.setLoadingCallback(this.loadingCallback);
            }
        }
    }

    public interface LoadingCallback {

        void onLoadRequested();

    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {

        private StreamImageView view;

        private ImageViewHolder(View itemView) {
            super(itemView);
            this.view = (StreamImageView) itemView;
        }

        private void bind(
                final Image image,
                ImageStreamFragment fragment,
                final ImageSelectedCallback listener
        ) {
            view.bind(fragment, image, listener);
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private LoadingView loadingView;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            this.loadingView = (LoadingView) itemView;
        }
    }

}
