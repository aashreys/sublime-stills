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

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.adapters.CollectionsAdapterModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 13/04/17.
 */

public class CollectionsActivityModel implements ViewModel, CollectionRepository
        .CollectionRepositoryListener {

    private final CollectionsAdapterModel collectionsAdapterModel;

    @Inject CollectionRepository collectionRepository;

    private EventListener eventListener;

    CollectionsActivityModel() {
        this.collectionsAdapterModel = new CollectionsAdapterModel();
    }

    void onInjectionComplete() {
        collectionsAdapterModel.addCollectionList(collectionRepository.getAll());
        collectionRepository.addListener(this);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    void onCollectionClicked(int position) {
        saveCollections();
        if (eventListener != null) {
            eventListener.onCollectionClicked(position);
        }
    }

    void onActivityDestroyed() {
        this.collectionRepository.removeListener(this);
        saveCollections();
    }

    private void saveCollections() {
        collectionsAdapterModel.saveCollections(collectionRepository);
    }

    @Override
    public void onInsert(Collection object) {
        collectionsAdapterModel.addCollection(object);
    }

    @Override
    public void onUpdate(Collection object) {

    }

    @Override
    public void onDelete(Collection object) {
        collectionsAdapterModel.removeCollection(object);
    }

    @Override
    public void onReplaceAll(List<Collection> collections) {

    }

    CollectionsAdapterModel getCollectionsAdapterModel() {
        return collectionsAdapterModel;
    }

    interface EventListener {

        void onCollectionClicked(int position);

    }

}
