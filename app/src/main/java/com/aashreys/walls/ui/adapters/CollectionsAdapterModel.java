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

package com.aashreys.walls.ui.adapters;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aashreys on 13/04/17.
 */

public class CollectionsAdapterModel {

    private final List<Collection> collectionList;

    private boolean isCollectionListModified;

    private EventListener eventListener;

    public CollectionsAdapterModel() {
        this.collectionList = new ArrayList<>();
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
        if (eventListener != null) {
            eventListener.onCollectionListChanged();
        }
    }

    public void addCollection(Collection collection) {
        if (!collectionList.contains(collection)) {
            collectionList.add(collection);
            if (eventListener != null) {
                eventListener.onCollectionAdded(collectionList.size() - 1);
            }
        }
    }

    public void addCollectionList(List<Collection> collectionList) {
        this.collectionList.addAll(collectionList);
        if (eventListener != null) {
            eventListener.onCollectionListChanged();
        }
    }

    public void removeCollection(Collection collection) {
        int position = collectionList.indexOf(collection);
        if (position != -1) {
            collectionList.remove(position);
            if (eventListener != null) {
                eventListener.onCollectionRemoved(position);
            }
        }
    }

    int collectionListSize() {
        return collectionList.size();
    }

    Collection getCollection(int position) {
        return collectionList.get(position);
    }

    void moveCollection(int fromPosition, int toPosition) {
        Collection collection = collectionList.remove(fromPosition);
        collectionList.add(toPosition, collection);
        isCollectionListModified = true;
    }

    public void saveCollections(CollectionRepository collectionRepository) {
        if (isCollectionListModified) {
            collectionRepository.replaceAll(collectionList);
            isCollectionListModified = false;
        }
    }

    interface EventListener {

        void onCollectionAdded(int position);

        void onCollectionRemoved(int position);

        void onCollectionListChanged();

    }

}
