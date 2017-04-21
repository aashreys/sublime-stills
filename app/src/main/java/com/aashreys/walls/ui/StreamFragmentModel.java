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

package com.aashreys.walls.ui;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager;
import com.aashreys.walls.ui.adapters.StreamAdapter;
import com.aashreys.walls.ui.helpers.NetworkHelper;
import com.aashreys.walls.ui.tasks.LoadImagesTask;
import com.aashreys.walls.ui.views.StreamImageView;
import com.aashreys.walls.utils.LogWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.END_OF_COLLECTION;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.FAVORITE;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.GENERIC_ERROR;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.LOADING;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.NOT_LOADING;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.NO_INTERNET;
import static com.aashreys.walls.ui.StreamFragment.LoadingViewStateManager.State.SLOW_INTERNET;

/**
 * Created by aashreys on 17/04/17.
 */

public class StreamFragmentModel implements StreamAdapter.LoadingCallback,
        LoadImagesTask.LoadCallback, StreamAdapter.ImageProvider {

    private static final String TAG = StreamFragmentModel.class.getSimpleName();

    @Inject Lazy<FavoriteImageRepository> favoriteImageRepositoryLazy;

    @Inject DeviceInfo deviceInfo;

    @Inject SourceFactory sourceFactory;

    @Inject NetworkHelper networkHelper;

    private Collection collection;

    @Nullable private FavoriteImageRepository favoriteImageRepository;

    @Nullable private RepositoryCallback<Image> favoriteRepoListener;

    private StreamImageView.InteractionCallback imageInteractionListener;

    private LoadImagesTask loadImagesTask;

    private boolean isDisplayed;

    private EventListener eventListener;

    private List<Image> imageList;

    @Inject
    public StreamFragmentModel() {
        imageList = new ArrayList<>();
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

    void loadMoreImages() {
        LogWrapper.i(TAG, collection.getName().value() + " - loading images");
        if (loadImagesTask != null) {
            loadImagesTask.release();
            loadImagesTask = null;
        }
        loadImagesTask = new LoadImagesTask(sourceFactory.create(collection), this);
        loadImagesTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                imageList.size()
        );
        if (eventListener != null) {
            @LoadingViewStateManager.State int loadingState;
            if (!isFavoriteStream()) {
                if (!networkHelper.isConnected()) {
                    loadingState = NO_INTERNET;
                } else if (!networkHelper.isFastNetworkConnected()) {
                    loadingState = SLOW_INTERNET;
                } else {
                    loadingState = LOADING;
                }
            } else {
                loadingState = FAVORITE;
            }
            eventListener.onLoadingUiStateChanged(loadingState);
        }
    }

    @Override
    public void onLoadComplete(@NonNull List<Image> images) {
        int oldPosition = imageList.size();
        imageList.addAll(images);
        if (eventListener != null) {
            eventListener.onImagesAdded(oldPosition, images.size());
            if (isFavoriteStream()) {
                eventListener.onLoadingUiStateChanged(FAVORITE);
            } else {
                if (images.size() > 0) {
                    eventListener.onLoadingUiStateChanged(NOT_LOADING);
                } else {
                    eventListener.onLoadingUiStateChanged(END_OF_COLLECTION);
                }
            }
        }
    }

    @Override
    public void onLoadError() {
        if (eventListener != null) {
            eventListener.onLoadingUiStateChanged(GENERIC_ERROR);
        }
    }

    @Override
    public void onLoadRequested() {
        if (loadImagesTask == null || !loadImagesTask.isLoading()) {
            loadMoreImages();
        }
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
        imageInteractionListener = null;
        stopListeningToFavoritesRepo();
        if (loadImagesTask != null) {
            loadImagesTask.release();
            loadImagesTask = null;
        }
    }

    void onInjectionComplete() {

    }

    interface EventListener {

        void onImagesAdded(int positionStart, int itemCount);

        void onImageAdded(int position);

        void onImageRemoved(int position);

        void onLoadingUiStateChanged(@LoadingViewStateManager.State int loadingState);

    }
}
