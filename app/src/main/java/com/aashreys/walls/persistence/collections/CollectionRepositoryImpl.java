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

package com.aashreys.walls.persistence.collections;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.models.CollectionModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * Created by aashreys on 01/02/17.
 */

public class CollectionRepositoryImpl implements CollectionRepository {

    private static final String TAG = CollectionRepositoryImpl.class.getSimpleName();

    private final static List<CollectionRepositoryListener> listeners = new ArrayList<>();

    public static final String BOOK_NAME = "collections_book";

    private final CollectionModelFactory collectionModelFactory;

    public CollectionRepositoryImpl(CollectionModelFactory collectionModelFactory) {
        this.collectionModelFactory = collectionModelFactory;
    }

    @Override
    public void insert(Collection collection) {
        CollectionModel model = createModel(collection);
        getBook().write(getModelKey(model), model);
        _notifyInsert(collection);
    }

    @Override
    public boolean contains(Collection collection) {
        CollectionModel model = createModel(collection);
        return getBook().exist(getModelKey(model));
    }

    @Override
    public void remove(Collection collection) {
        CollectionModel model = createModel(collection);
        getBook().delete(getModelKey(model));
        _notifyDelete(collection);
    }

    @Override
    public void replaceAll(List<Collection> collectionList) {
        getBook().destroy();
        Book book = getBook();
        for (Collection collection : collectionList) {
            CollectionModel model = createModel(collection);
            book.write(getModelKey(model), model);
        }
        _notifyReplaceAll(collectionList);
    }

    @Override
    public int size() {
        return getBook().getAllKeys().size();
    }

    @NonNull
    @Override
    public List<Collection> getAll() {
        List<Collection> collectionList = new ArrayList<>();
        List<String> keyList = getBook().getAllKeys();
        Book book = getBook();
        for (String key : keyList) {
            CollectionModel model = book.read(key, null);
            Collection collection = model.createCollection();
            if (collection != null) {
                collectionList.add(collection);
            }
        }
        return collectionList;
    }

    @Override
    public void addListener(CollectionRepositoryListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CollectionRepositoryListener listener) {
        listeners.remove(listener);
    }

    private void _notifyDelete(Collection collection) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onDelete(collection);
        }
    }

    private void _notifyInsert(Collection collection) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onInsert(collection);
        }
    }

    private void _notifyReplaceAll(List<Collection> collections) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onReplaceAll(collections);
        }
    }

    private Book getBook() {
        return Paper.book(BOOK_NAME);
    }

    private CollectionModel createModel(Collection collection) {
        return collectionModelFactory.create(collection);
    }

    private String getModelKey(CollectionModel model) {
        return model.getClass().getSimpleName() + model.getId();
    }

}
