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

package com.aashreys.walls.application.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.application.fragments.StreamFragment;
import com.aashreys.walls.application.views.StreamImageView;

import static com.aashreys.walls.application.views.StreamImageView.InteractionCallback;

/**
 * Created by aashreys on 04/02/17.
 */

public class StreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = StreamAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_IMAGE = 0, VIEW_TYPE_LOADING = 1;

    @NonNull
    private final StreamFragment fragment;

    @NonNull
    private final InteractionCallback streamImageViewInteractionCallback;

    private View loadingView;

    private ImageProvider imageProvider;

    private boolean isLoadingEnabled;

    /**
     * How many items from the end of the list should the adapter request for more data to be
     * loaded via an instance of {@link LoadingCallback} set via
     * {@link #setLoadingCallback(LoadingCallback)}
     */
    private int loadingThreshold = 5;

    private LoadingCallback loadingCallback;

    public StreamAdapter(
            @NonNull StreamFragment fragment,
            @NonNull InteractionCallback listener,
            @NonNull ImageProvider imageProvider
    ) {
        this.fragment = fragment;
        this.streamImageViewInteractionCallback = listener;
        this.imageProvider = imageProvider;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        notifyItemInserted(imageProvider.size());
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
                return new RecyclerView.ViewHolder(loadingView) {};

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(
                    imageProvider.getImage(position),
                    fragment,
                    streamImageViewInteractionCallback
            );
        }
        int thresholdDiff = getItemCount() - position;
        if (isLoadingEnabled && loadingThreshold >= thresholdDiff && loadingCallback != null) {
            loadingCallback.onLoadRequested();
        }
    }

    @Override
    public int getItemCount() {
        if (loadingView != null) {
            return imageProvider.size() + 1;
        } else {
            return imageProvider.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (loadingView != null) {
            if (position < getItemCount() - 1) {
                return VIEW_TYPE_IMAGE;
            } else {
                return VIEW_TYPE_LOADING;
            }
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }

    /**
     * Set how many items before the end of the image list should this adapter request a new load.
     * See {@link StreamAdapter#loadingThreshold}.
     */
    public void setLoadingThreshold(int lastNItems) {
        this.loadingThreshold = lastNItems;
    }

    public void setLoadingCallback(LoadingCallback loadingCallback) {
        this.loadingCallback = loadingCallback;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void setIsLoadingEnabled(boolean isLoadingEnabled) {
        this.isLoadingEnabled = isLoadingEnabled;
    }

    public interface LoadingCallback {

        void onLoadRequested();

    }

    public interface CollectionProvider {

        Collection getCollection(int position);

        int size();

    }

    public interface ImageProvider {

        Image getImage(int position);

        int size();

    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {

        private StreamImageView view;

        private ImageViewHolder(View itemView) {
            super(itemView);
            this.view = (StreamImageView) itemView;
        }

        private void bind(
                final Image image,
                StreamFragment fragment,
                final InteractionCallback listener
        ) {
            view.bind(fragment, image, listener);
        }
    }
}
