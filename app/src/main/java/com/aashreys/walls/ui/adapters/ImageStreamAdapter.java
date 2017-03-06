package com.aashreys.walls.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.ui.ImageStreamFragment;
import com.aashreys.walls.ui.views.StreamImageView;

import java.util.ArrayList;
import java.util.List;

import static com.aashreys.walls.ui.ImageStreamFragment.ImageSelectedCallback;

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

    private final List<Image> imageList;

    private int loadingThreshold = 5;

    private LoadMoreCallback loadMoreCallback;

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
        int oldSize = imageList.size();
        imageList.addAll(images);
        if (oldSize != 0) {
            notifyItemRangeInserted(oldSize, images.size());
        } else {
            notifyDataSetChanged();
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
                                .inflate(R.layout.item_image, parent, false)
                );

            case VIEW_TYPE_LOADING:
                loadingViewHolder = new LoadingViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.layout_view_loading, parent, false)
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
        if (loadingThreshold >= thresholdDiff && loadMoreCallback != null) {
            loadMoreCallback.onLoadMore();
            if (loadingViewHolder != null) {
                loadingViewHolder.showLoadingView();
            }
        }
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
     * Tells the adapter that loading is complete and that loading progress should no longer be
     * displayed.
     */
    public void onLoadComplete() {
        if (loadingViewHolder != null) {
            loadingViewHolder.hideLoadingView();
        }
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setLoadingThreshold(int lastNItems) {
        this.loadingThreshold = lastNItems;
    }

    public void setLoadMoreCallback(LoadMoreCallback loadMoreCallback) {
        this.loadMoreCallback = loadMoreCallback;
    }

    public interface LoadMoreCallback {

        void onLoadMore();

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

        private ProgressBar progressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView;
        }

        private void showLoadingView() {
            progressBar.setVisibility(View.VISIBLE);
        }

        private void hideLoadingView() {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

}
