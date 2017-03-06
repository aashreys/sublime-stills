package com.aashreys.walls.ui.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.ImageStreamFragment;
import com.aashreys.walls.ui.helpers.GlideHelper;
import com.aashreys.walls.ui.utils.ForegroundImageView;
import com.bumptech.glide.Priority;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/02/17.
 */

public class StreamImageView extends FrameLayout {

    @Inject FavoriteImageRepository favoriteImageRepository;

    private FavoriteSyncTask favoriteSyncTask;

    private ForegroundImageView imageView;

    private ImageButton favoriteButton;

    private Image image;

    private RepositoryCallback<Image> repositoryCallback = new RepositoryCallback<Image>() {
        @Override
        public void onInsert(Image object) {
            onFavoriteStateChanged(object, true);
        }

        @Override
        public void onUpdate(Image object) {

        }

        @Override
        public void onDelete(Image object) {
            onFavoriteStateChanged(object, false);
        }
    };

    public StreamImageView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    public StreamImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public StreamImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StreamImageView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
        LayoutInflater.from(context).inflate(R.layout.layout_item_image, this, true);
        imageView = (ForegroundImageView) findViewById(R.id.image);
        favoriteButton = (ImageButton) findViewById(R.id.button_action);
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                favoriteImageRepository.addListener(repositoryCallback);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                favoriteImageRepository.removeListener(repositoryCallback);
            }
        });
    }

    public void bind(
            ImageStreamFragment fragment,
            final Image image,
            final ImageStreamFragment.ImageSelectedCallback listener
    ) {
        this.image = image;
        favoriteButton.setVisibility(View.GONE);
        GlideHelper.displayImageAsync(
                fragment,
                image.getUrl(Image.UrlType.IMAGE_STREAM),
                imageView,
                fragment.isDisplayed() ? Priority.HIGH : Priority.LOW
        );
        if (listener != null) {
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageSelected(image);
                }
            });
        }
        syncFavoriteState();
    }

    private void syncFavoriteState() {
        if (favoriteSyncTask != null &&
                favoriteSyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            favoriteSyncTask.cancel(true);
            favoriteSyncTask = null;
        }
        favoriteSyncTask = new FavoriteSyncTask(image, favoriteImageRepository) {
            @Override
            protected void onSyncComplete(Image oldImage, final boolean isFavorite) {
                onFavoriteStateChanged(oldImage, isFavorite);
            }
        };
        favoriteSyncTask.execute();
    }

    private void onFavoriteStateChanged(Image newImage, final boolean isFavorite) {
        // Check equality since favorite state is relayed on a background thread and this view
        // may have been rebound to a different image by then.
        if (newImage.equals(image)) {
            favoriteButton.setVisibility(View.VISIBLE);
            favoriteButton.setImageResource(isFavorite
                    ? R.drawable.ic_favorite_light_24dp
                    : R.drawable.ic_favorite_border_light_24dp
            );
            favoriteButton.setOnClickListener(
                    new View.OnClickListener() {
                        private boolean isFavorite2 = isFavorite;

                        @Override
                        public void onClick(View v) {
                            if (isFavorite2) {
                                favoriteImageRepository.unfavorite(image);
                                favoriteButton.setImageResource(R.drawable
                                        .ic_favorite_border_light_24dp);
                            } else {
                                favoriteImageRepository.favorite(image);
                                favoriteButton.setImageResource(R.drawable
                                        .ic_favorite_light_24dp);
                            }
                            isFavorite2 = !isFavorite2;
                        }
                    }
            );
        }
    }

    private static abstract class FavoriteSyncTask extends AsyncTask<Void, Void, Boolean> {

        private Image image;

        private FavoriteImageRepository repository;

        private FavoriteSyncTask(Image image, FavoriteImageRepository repository) {
            this.image = image;
            this.repository = repository;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return repository.isFavorite(image);
        }

        @Override
        protected void onPostExecute(Boolean isFavorite) {
            super.onPostExecute(isFavorite);
            onSyncComplete(image, isFavorite);
        }

        protected abstract void onSyncComplete(Image image, boolean isFavorite);
    }

}
