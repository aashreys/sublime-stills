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

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.persistence.collections.CollectionRepository;

import org.apache.commons.lang3.text.WordUtils;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/04/17.
 */

public class CollectionViewModel implements ViewModel {

    private final CollectionRepository collectionRepository;

    private Collection collection;

    @Nullable
    private EventCallback eventCallback;

    @Inject
    public CollectionViewModel(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    void onRemoveButtonClicked() {
        if (collection != null && collection.isRemovable()) {
            collectionRepository.remove(collection);
        }
    }

    String getCollectionName() {
        return collection != null ? WordUtils.capitalizeFully(collection.getName().value()) : null;
    }

    @DrawableRes
    int getCollectionIcon() {
        int iconRes = 0;
        if (collection != null) {
            if (collection instanceof FavoriteCollection) {
                iconRes = R.drawable.ic_favorite_black_24dp;
            } else {
                iconRes = R.drawable.ic_camera_black_24dp;
            }
        }
        return iconRes;
    }

    void setCollection(Collection collection) {
        this.collection = collection;
        if (eventCallback != null) {
            eventCallback.onCollectionSet();
        }
    }

    @DrawableRes
    int getDeleteButtonIcon() {
        int iconRes = 0;
        if (collection != null) {
            iconRes = collection.isRemovable() ?
                    R.drawable.ic_delete_black_24dp :
                    R.drawable.ic_delete_black_inactive_24dp;
        }
        return iconRes;
    }

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    interface EventCallback {

        void onCollectionSet();

    }
}
