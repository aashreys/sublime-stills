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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.ui.StreamFragment;
import com.aashreys.walls.ui.views.StreamImageView;

import java.util.ArrayList;
import java.util.List;

import static com.aashreys.walls.ui.views.StreamImageView.InteractionCallback;

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

    @NonNull
    private final List<Image> imageList;

    private View loadingView;

    /**
     * How many items from the end of the list should the adapter request for more data to be
     * loaded via an instance of {@link LoadingCallback} set via
     * {@link #setLoadingCallback(LoadingCallback)}
     */
    private int loadingThreshold = 5;

    private LoadingCallback loadingCallback;

    public StreamAdapter(
            @NonNull StreamFragment fragment,
            @NonNull InteractionCallback listener
    ) {
        this.fragment = fragment;
        this.streamImageViewInteractionCallback = listener;
        this.imageList = new ArrayList<>();
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        notifyItemInserted(imageList.size());
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
                return new RecyclerView.ViewHolder(loadingView) {};

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
                    streamImageViewInteractionCallback
            );
        }
        int currentPosition = holder.getAdapterPosition();
        int thresholdDiff = getItemCount() - currentPosition;
        if (loadingThreshold >= thresholdDiff && loadingCallback != null) {
            loadingCallback.onLoadRequested();
        }
    }

    public int getImageCount() {
        return imageList.size();
    }

    @Override
    public int getItemCount() {
        if (loadingView != null) {
            return imageList.size() + 1;
        } else {
            return imageList.size();
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

    public interface LoadingCallback {

        void onLoadRequested();

    }

    public interface CollectionProvider {

        Collection getCollection(int position);

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
