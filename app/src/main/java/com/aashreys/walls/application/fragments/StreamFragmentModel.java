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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.application.adapters.StreamAdapter;
import com.aashreys.walls.application.helpers.NetworkHelper;
import com.aashreys.walls.application.views.StreamImageView;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.utils.LogWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by aashreys on 17/04/17.
 */

public class StreamFragmentModel implements StreamAdapter.LoadingCallback,
        StreamAdapter.ImageProvider {

    private static final String TAG = StreamFragmentModel.class.getSimpleName();

    private final Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy;

    private final DeviceInfo deviceInfo;

    private final SourceFactory sourceFactory;

    private final NetworkHelper networkHelper;

    private final List<Image> imageList;

    private Collection collection;

    @Nullable private FavoriteImageRepository favoriteImageRepository;

    @Nullable private RepositoryCallback<Image> favoriteRepoListener;

    private StreamImageView.InteractionCallback imageInteractionListener;

    private EventListener eventListener;

    private Disposable imageDisposable;

    private boolean isLoading, isDisplayed;

    private SchedulerProvider schedulerProvider;

    private StreamScrollListener streamScrollListener;

    @Inject
    StreamFragmentModel(
            Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy,
            DeviceInfo deviceInfo,
            SourceFactory sourceFactory,
            NetworkHelper networkHelper,
            SchedulerProvider schedulerProvider
    ) {
        this.favoriteImageRepositoryLazy = favoriteImageRepositoryLazy;
        this.deviceInfo = deviceInfo;
        this.sourceFactory = sourceFactory;
        this.networkHelper = networkHelper;
        this.schedulerProvider = schedulerProvider;
        this.imageList = new ArrayList<>();
    }

    public void setStreamScrollListener(StreamScrollListener listener) {
        this.streamScrollListener = listener;
    }

    void setCollection(Collection collection) {
        this.collection = collection;
    }

    private boolean isFavoriteStream() {
        return collection != null && collection instanceof FavoriteCollection;
    }

    void setIsDisplayed(boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    boolean isDisplayed() {
        return isDisplayed;
    }

    int getNumberOfStreamColumns() {
        return deviceInfo.getNumberOfStreamColumns();
    }

    void onFragmentReady() {
        if (isFavoriteStream()) {
            startListeningToFavoritesRepo();
        } else {
            stopListeningToFavoritesRepo();
        }
        loadMoreImages();
    }

    private void stopListeningToFavoritesRepo() {
        if (favoriteImageRepository != null && favoriteRepoListener != null) {
            favoriteImageRepository.removeListener(favoriteRepoListener);
        }
    }

    private void startListeningToFavoritesRepo() {
        favoriteImageRepository = favoriteImageRepositoryLazy.get();
        favoriteRepoListener = new RepositoryCallback<Image>() {
            @Override
            public void onInsert(Image image) {
                imageList.add(0, image);
                if (eventListener != null) {
                    eventListener.onImageAdded(0);
                }
            }

            @Override
            public void onUpdate(Image object) {

            }

            @Override
            public void onDelete(Image image) {
                int index = imageList.indexOf(image);
                if (index != -1) {
                    imageList.remove(image);
                    if (eventListener != null) {
                        eventListener.onImageRemoved(index);
                    }
                }
            }
        };
        //noinspection ConstantConditions
        favoriteImageRepository.addListener(favoriteRepoListener);
    }

    private void disposeImageDisposable() {
        if (imageDisposable != null && !imageDisposable.isDisposed()) {
            imageDisposable.dispose();
            imageDisposable = null;
        }
    }

    private void loadMoreImages() {
        if (!isLoading) {
            LogWrapper.d(TAG, collection.getName().value() + " - loading images");
            disposeImageDisposable();
            Source source = sourceFactory.create(collection);
            source.getImages(imageList.size())
                    .subscribeOn(schedulerProvider.io())
                    .unsubscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe(new SingleObserver<List<Image>>() {
                        @Override
                        public void onSubscribe(
                                @io.reactivex.annotations.NonNull Disposable disposable
                        ) {
                            imageDisposable = disposable;
                        }

                        @Override
                        public void onSuccess(
                                @io.reactivex.annotations.NonNull List<Image> images
                        ) {
                            onLoadComplete(images);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable throwable) {
                            onLoadError(throwable);
                        }
                    });
            isLoading = true;
            if (eventListener != null) {
                @StreamFragment.LoadingViewStateManager.State int loadingState;
                if (!isFavoriteStream()) {
                    if (!networkHelper.isConnected()) {
                        eventListener.onNoNetworkError();
                    } else if (!networkHelper.isFastNetworkConnected()) {
                        eventListener.onSlowNetworkError();
                    } else {
                        eventListener.onImagesLoading(false);
                    }
                } else {
                    eventListener.onImagesLoading(true);
                }
            }
        }
    }

    private void onLoadComplete(@NonNull List<Image> images) {
        isLoading = false;
        int oldPosition = imageList.size();
        imageList.addAll(images);
        if (eventListener != null) {
            eventListener.onImagesAdded(oldPosition, images.size());
            if (images.size() > 0) {
                eventListener.onImageLoadingComplete(isFavoriteStream());
            } else {
                eventListener.onCollectionEndReached(isFavoriteStream());
            }
        }
    }

    private void onLoadError(Throwable throwable) {
        isLoading = false;
        LogWrapper.e(TAG, "Failed to load image stream", throwable);
        if (eventListener != null) {
            eventListener.onGenericError();
        }
    }

    @Override
    public void onLoadRequested() {
        loadMoreImages();
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    StreamImageView.InteractionCallback getImageInteractionListener() {
        return imageInteractionListener;
    }

    void setImageInteractionListener(StreamImageView.InteractionCallback imageInteractionListener) {
        this.imageInteractionListener = imageInteractionListener;
    }

    @Override
    public Image getImage(int position) {
        return imageList.get(position);
    }

    @Override
    public int size() {
        return imageList.size();
    }

    void onFragmentViewDestroyed() {
        disposeImageDisposable();
        imageInteractionListener = null;
        stopListeningToFavoritesRepo();
    }

    void notifyStreamScrollUp() {
        if (streamScrollListener != null) {
            streamScrollListener.onStreamScrolledUp();
        }
    }

    void notifyStreamScrollDown() {
        if (streamScrollListener != null) {
            streamScrollListener.onStreamScrolledDown();
        }
    }

    interface EventListener {

        void onImagesAdded(int positionStart, int itemCount);

        void onImageAdded(int position);

        void onImageRemoved(int position);

        void onImagesLoading(boolean isFavorite);

        void onNoNetworkError();

        void onSlowNetworkError();

        void onImageLoadingComplete(boolean isFavorite);

        void onCollectionEndReached(boolean isFavorite);

        void onGenericError();

    }

    public interface StreamScrollListener {

        void onStreamScrolledUp();

        void onStreamScrolledDown();

    }
}
