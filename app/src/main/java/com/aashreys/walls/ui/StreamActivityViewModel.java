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

import android.support.annotation.Nullable;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.views.StreamImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 08/04/17.
 */

public class StreamActivityViewModel extends ActivityViewModel implements StreamImageView.InteractionCallback,
        CollectionRepository.CollectionRepositoryListener, StreamActivity.CollectionProvider {

    public static final String KEY_IS_ONBOARDING_COMPLETED = "key_is_onboarding_completed";

    private final List<Collection> collectionsList =
            Collections.synchronizedList(new ArrayList<Collection>());

    @Inject FavoriteImageRepository favoriteImageRepository;

    @Inject CollectionRepository collectionRepository;

    @Inject CollectionFactory collectionFactory;

    @Inject KeyValueStore keyValueStore;

    @Nullable private StreamActivityModelEventListener eventListener;

    @Override
    protected void didCreate() {
        super.didCreate();
        collectionsList.addAll(collectionRepository.getAll());
    }

    void setEventListener(@Nullable StreamActivityModelEventListener eventListener) {
        this.eventListener = eventListener;
    }

    boolean shouldOnboardingBeDisplayed() {
        return !keyValueStore.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false);
    }

    void beforeActivityInit() {
        addDefaultCollectionsIfMissing();
    }

    private void addDefaultCollectionsIfMissing() {
        DiscoverCollection discoverCollection = (DiscoverCollection) collectionFactory.create(
                Collection.Type.DISCOVER,
                null,
                null
        );
        FavoriteCollection favoriteCollection = (FavoriteCollection) collectionFactory.create(
                Collection.Type.FAVORITE,
                null,
                null
        );
        if (!collectionRepository.exists(discoverCollection)) {
            collectionRepository.insert(discoverCollection);
        }
        if (!collectionRepository.exists(favoriteCollection)) {
            collectionRepository.insert(favoriteCollection);
        }
    }

    boolean onNavigationItemSelected(int itemId) {
        switch (itemId) {

            case R.id.menu_item_settings:
                if (eventListener != null) {
                    eventListener.onSettingsNavigationItemSelected();
                    return true;
                }

            case R.id.menu_item_collections:
                if (eventListener != null) {
                    eventListener.onCollectionsNavigationItemSelected();
                    return true;
                }
        }
        return false;
    }

    void onReceiveTabPositionFromIntent(int tabPosition, int totalTabs) {
        if (totalTabs > 0 && tabPosition >= 0 && tabPosition < totalTabs) {
            if (eventListener != null) {
                eventListener.onNewTabSelected(tabPosition);
            }
        }
    }

    @Override
    public void onImageClicked(Image image) {
        if (eventListener != null) {
            eventListener.onImageClicked(image);
        }
    }

    @Override
    public void onFavoriteButtonClicked(Image image, boolean isFavorited) {
        if (isFavorited) {
            favoriteImageRepository.favorite(image);
        } else {
            favoriteImageRepository.unfavorite(image);
        }
        if (eventListener != null) {
            eventListener.onImageFavoriteButtonClicked(image, isFavorited);
        }
    }

    void onFavoriteRemovedUndoButtonClicked(Image image) {
        favoriteImageRepository.favorite(image);
    }

    void onMenuButtonClicked() {
        if (eventListener != null) {
            eventListener.onMenuButtonClicked();
        }
    }

    void onAddCollectionButtonClicked() {
        if (eventListener != null) {
            eventListener.onAddCollectionsButtonClicked();
        }
    }

    @Override
    public void onInsert(Collection object) {
        collectionsList.add(object);
        if (eventListener != null) {
            eventListener.onCollectionsModified();
        }
    }

    @Override
    public void onUpdate(Collection object) {

    }

    @Override
    public void onDelete(Collection object) {
        collectionsList.remove(object);
        if (eventListener != null) {
            eventListener.onCollectionsModified();
        }
    }

    @Override
    public void onReplaceAll(List<Collection> collections) {
        collectionsList.clear();
        collectionsList.addAll(collections);
        if (eventListener != null) {
            eventListener.onCollectionsModified();
        }
    }

    @Override
    public Collection getCollection(int position) {
        return collectionsList.get(position);
    }

    @Override
    public int size() {
        return collectionsList.size();
    }
}
