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

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.StreamFragment;
import com.aashreys.walls.ui.helpers.ImageDownloader;
import com.bumptech.glide.Priority;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/04/17.
 */

public class StreamImageViewModel implements ViewModel, RepositoryCallback<Image> {

    @Inject DeviceInfo deviceInfo;

    @Inject FavoriteImageRepository favoriteImageRepository;

    @Inject ImageDownloader imageDownloader;

    private FavoriteSyncTask favoriteSyncTask;

    private StreamImageView.InteractionCallback interactionCallback;

    private EventCallback eventCallback;

    private Image image;

    private boolean isFavorite;

    private boolean isFavoriteSyncComplete;

    private int getIdealImageWidth() {
        return deviceInfo.getDeviceResolution().getWidth() / deviceInfo.getNumberOfStreamColumns();
    }

    void onViewOnScreen() {
        favoriteImageRepository.addListener(this);
    }

    void onViewOffScreen() {
        favoriteImageRepository.removeListener(this);
    }

    void setInteractionCallback(StreamImageView.InteractionCallback interactionCallback) {
        this.interactionCallback = interactionCallback;
    }

    void setData(StreamFragment fragment, ImageView imageView, Image image) {
        this.image = image;
        downloadImage(fragment, imageView);
        syncFavoriteState();
    }

    void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    void onImageClicked() {
        if (interactionCallback != null && image != null) {
            interactionCallback.onImageClicked(image);
        }
    }

    private void syncFavoriteState() {
        isFavorite = false; // Invalidate favorite state
        isFavoriteSyncComplete = false;
        if (favoriteSyncTask != null && favoriteSyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            favoriteSyncTask.cancel(true);
            favoriteSyncTask = null;
        }
        favoriteSyncTask = new StreamImageViewModel.FavoriteSyncTask(
                image,
                favoriteImageRepository
        ) {
            @Override
            protected void onSyncComplete(Image oldImage, final boolean isFavorite) {
                evaluateFavoriteState(oldImage, isFavorite);
            }
        };
        favoriteSyncTask.execute();
        if (eventCallback != null) {
            eventCallback.onFavoriteSyncStarted();
        }
    }

    private void downloadImage(StreamFragment fragment, ImageView imageView) {
        imageDownloader.asDrawable(
                fragment,
                image.getUrl(getIdealImageWidth()),
                fragment.isDisplayed() ? Priority.IMMEDIATE : Priority.LOW,
                imageView,
                new ImageDownloader.Listener<Drawable>() {
                    @Override
                    public void onComplete(Drawable result) {
                        if (eventCallback != null) {
                            eventCallback.onImageDownloaded(result);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (eventCallback != null) {
                            eventCallback.onImageDownloadFailed();
                        }
                    }
                }
        );
    }

    private void evaluateFavoriteState(Image oldImage, boolean isFavorite) {
        // Check equality since favorite state is relayed on a background thread and this view
        // may have been rebound to a different image by then.
        if (oldImage.equals(image)) {
            isFavoriteSyncComplete = true;
            this.isFavorite = isFavorite;
            if (eventCallback != null) {
                eventCallback.onFavoriteStateChanged();
            }
        } else {
            this.isFavorite = false;
        }
    }

    @DrawableRes
    int getFavoriteButtonIconRes() {
        return isFavorite ?
                R.drawable.ic_favorite_light_24dp :
                R.drawable.ic_favorite_border_light_24dp;
    }

    void onFavoriteButtonClicked() {
        if (isFavoriteSyncComplete) {
            isFavorite = !isFavorite;
            if (eventCallback != null) {
                eventCallback.onFavoriteStateChanged();
            }
            if (interactionCallback != null) {
                interactionCallback.onFavoriteButtonClicked(image, isFavorite);
            }
        }
    }

    @Override
    public void onInsert(Image image) {
        evaluateFavoriteState(image, true);
    }

    @Override
    public void onUpdate(Image image) {

    }

    @Override
    public void onDelete(Image image) {
        evaluateFavoriteState(image, false);
    }

    interface EventCallback {

        void onImageDownloaded(Drawable image);

        void onImageDownloadFailed();

        void onFavoriteStateChanged();

        void onFavoriteSyncStarted();

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
