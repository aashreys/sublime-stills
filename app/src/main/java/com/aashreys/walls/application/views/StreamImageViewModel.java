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

package com.aashreys.walls.application.views;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.application.fragments.StreamFragment;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.utils.LogWrapper;
import com.bumptech.glide.Priority;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by aashreys on 09/04/17.
 */

public class StreamImageViewModel implements ViewModel, RepositoryCallback<Image> {

    private static final String TAG = StreamImageViewModel.class.getSimpleName();

    private final DeviceInfo deviceInfo;

    private final FavoriteImageRepository favoriteImageRepository;

    private final ImageDownloader imageDownloader;

    private FavoriteSyncTask favoriteSyncTask;

    private StreamImageView.InteractionCallback interactionCallback;

    private EventCallback eventCallback;

    private Image image;

    private boolean isFavorite;

    private boolean isFavoriteSyncComplete;

    private Disposable imageDownloadDisposable;

    private final SchedulerProvider schedulerProvider;

    @Inject
    public StreamImageViewModel(
            DeviceInfo deviceInfo,
            FavoriteImageRepository favoriteImageRepository,
            ImageDownloader imageDownloader,
            SchedulerProvider schedulerProvider
    ) {
        this.deviceInfo = deviceInfo;
        this.favoriteImageRepository = favoriteImageRepository;
        this.imageDownloader = imageDownloader;
        this.schedulerProvider = schedulerProvider;
    }

    private int getImageWidth() {
        return deviceInfo.getDeviceResolution().getWidth() / deviceInfo.getNumberOfStreamColumns();
    }

    void onViewAttachedToWindow() {
        favoriteImageRepository.addListener(this);
    }

    void onViewDetachedFromWindow() {
        favoriteImageRepository.removeListener(this);
        if (imageDownloadDisposable != null && !imageDownloadDisposable.isDisposed()) {
            imageDownloadDisposable.dispose();
        }
    }

    void setInteractionCallback(StreamImageView.InteractionCallback interactionCallback) {
        this.interactionCallback = interactionCallback;
    }

    void setData(StreamFragment fragment, ImageView imageView, Image image) {
        this.image = image;
        if (eventCallback != null) {
            eventCallback.onImageBackgroundChanged(
                    Value.getValidValue(image.getBackgroundColor(), 0)
            );
            downloadImage(fragment, imageView);
            syncFavoriteState();
        }
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
                image.getUrl(getImageWidth()),
                fragment.isDisplayed() ? Priority.IMMEDIATE : Priority.LOW,
                imageView
        )
                .subscribeOn(schedulerProvider.mainThread())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(new SingleObserver<Drawable>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        imageDownloadDisposable = disposable;
                    }

                    @Override
                    public void onSuccess(@NonNull Drawable drawable) {
                        if (eventCallback != null) {
                            eventCallback.onImageDownloaded(drawable);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        LogWrapper.w(TAG, "Image download failed", throwable);
                        if (eventCallback != null) {
                            eventCallback.onImageDownloadFailed();
                        }
                    }
                });
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

    float getWidthToHeightRatio() {
        return 16f / 10;
    }

    interface EventCallback {

        void onImageDownloaded(Drawable image);

        void onImageDownloadFailed();

        void onFavoriteStateChanged();

        void onFavoriteSyncStarted();

        void onImageBackgroundChanged(int color);

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
