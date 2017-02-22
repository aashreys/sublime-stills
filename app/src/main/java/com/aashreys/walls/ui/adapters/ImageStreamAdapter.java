package com.aashreys.walls.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ImageStreamAdapter extends RecyclerView.Adapter<ImageStreamAdapter.ImageViewHolder> {

    private static final String TAG = ImageStreamAdapter.class.getSimpleName();

    @NonNull
    private final ImageStreamFragment fragment;

    @NonNull
    private final ImageSelectedCallback imageSelectedCallback;

    private final List<Image> imageList;

    private int loadingThreshold = 5;

    private LoadMoreCallback loadMoreCallback;

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
        notifyItemRangeInserted(oldSize, images.size());
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
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        holder.bind(imageList.get(position), fragment, imageSelectedCallback);
        int currentPosition = holder.getAdapterPosition();
        int thresholdDiff = getItemCount() - currentPosition;
        if (loadingThreshold >= thresholdDiff && loadMoreCallback != null) {
            loadMoreCallback.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
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

    static class ImageViewHolder extends RecyclerView.ViewHolder {

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

}
