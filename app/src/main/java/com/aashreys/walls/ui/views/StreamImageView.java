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

package com.aashreys.walls.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.ImageStreamFragment;
import com.aashreys.walls.ui.helpers.ImageDownloader;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.utils.ForegroundImageView;
import com.bumptech.glide.Priority;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/02/17.
 */

public class StreamImageView extends FrameLayout {

    @Inject FavoriteImageRepository favoriteImageRepository;

    @Inject DeviceResolution deviceResolution;

    @Inject ImageDownloader imageDownloader;

    private FavoriteSyncTask favoriteSyncTask;

    private ForegroundImageView imageView;

    private ImageButton favoriteButton;

    private Image image;

    private ImageSelectedCallback callback;

    private int numStreamColumns;

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
        LayoutInflater.from(context).inflate(R.layout.layout_item_stream_image, this, true);
        numStreamColumns = UiHelper.getStreamColumnCount(getContext());
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
            final ImageSelectedCallback callback
    ) {
        this.image = image;
        this.callback = callback;
        favoriteButton.setVisibility(View.GONE);


        int width;
        if (UiHelper.isPortrait(getContext())) {
            width = deviceResolution.getPortraitWidth() / numStreamColumns;
        } else {
            width = deviceResolution.getPortraitHeight() / numStreamColumns;
        }
        imageDownloader.asDrawable(
                fragment,
                image.getUrl(width),
                fragment.isDisplayed() ? Priority.HIGH : Priority.LOW,
                imageView,
                new ImageDownloader.Listener<Drawable>() {
                    @Override
                    public void onComplete(Drawable result) {
                        setVisibility(VISIBLE);
                        imageView.setAlpha(0f);
                        imageView.setImageDrawable(result);
                        imageView.animate().alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                    }

                    @Override
                    public void onError(Exception e) {
                        setVisibility(GONE);
                    }
                }
        );
        if (callback != null) {
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onImageSelected(image);
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
            favoriteButton.setImageResource(isFavorite
                    ? R.drawable.ic_favorite_light_24dp
                    : R.drawable.ic_favorite_border_light_24dp
            );
            favoriteButton.setVisibility(View.VISIBLE);
            favoriteButton.setOnClickListener(
                    new View.OnClickListener() {
                        private boolean isFavorite2 = isFavorite;

                        @Override
                        public void onClick(View v) {
                            if (isFavorite2) {
                                favoriteImageRepository.unfavorite(image);
                                favoriteButton.setImageResource(R.drawable
                                        .ic_favorite_border_light_24dp);
                                callback.onImageUnfavorited(image);
                            } else {
                                favoriteImageRepository.favorite(image);
                                favoriteButton.setImageResource(R.drawable
                                        .ic_favorite_light_24dp);
                                callback.onImageFavorited(image);
                            }
                            isFavorite2 = !isFavorite2;
                        }
                    }
            );
        }
    }

    public interface ImageSelectedCallback {

        void onImageSelected(Image image);

        void onImageFavorited(Image image);

        void onImageUnfavorited(Image image);

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
