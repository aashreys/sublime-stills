package com.aashreys.walls.persistence.collections;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionFactory;

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

    private static final String BOOK_NAME = "collections_book";

    private final CollectionFactory collectionFactory;

    public CollectionRepositoryImpl(CollectionFactory collectionFactory) {
        this.collectionFactory = collectionFactory;
    }

    @Override
    public void insert(Collection collection) {
        getBook().write(getCollectionKey(collection), collection);
        _notifyInsert(collection);
    }

    @Override
    public boolean exists(Collection collection) {
        return getBook().exist(getCollectionKey(collection));
    }

    @Override
    public void remove(Collection collection) {
        getBook().delete(getCollectionKey(collection));
        _notifyDelete(collection);
    }

    @Override
    public void replaceAll(List<Collection> collectionList) {
        getBook().destroy();
        Book book = getBook();
        for (Collection collection : collectionList) {
            book.write(getCollectionKey(collection), collection);
        }
        _notifyReplaceAll(collectionList);
    }

    @NonNull
    @Override
    public List<Collection> getAll() {
        List<Collection> collectionList = new ArrayList<>();
        List<String> keyList = getBook().getAllKeys();
        Book book = getBook();
        for (String key : keyList) {
            Collection collection = book.read(key, null);
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

    private String getCollectionKey(Collection collection) {
        return collection.getType() + collection.getId().value();
    }

}
