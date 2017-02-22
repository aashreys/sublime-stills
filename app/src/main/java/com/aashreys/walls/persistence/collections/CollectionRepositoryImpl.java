package com.aashreys.walls.persistence.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aashreys.walls.Utils;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aashreys on 01/02/17.
 */

public class CollectionRepositoryImpl implements CollectionRepository {

    private static final String TAG = CollectionRepositoryImpl.class.getSimpleName();

    private final static List<CollectionRepositoryListener> listeners = new ArrayList<>();

    private final CollectionFactory collectionFactory;

    public CollectionRepositoryImpl(CollectionFactory collectionFactory) {
        this.collectionFactory = collectionFactory;
    }

    @Override
    public void insert(Collection collection) {
        CollectionModel model = new CollectionModel(
                collection.name().value(),
                Utils.getCollectionType(collection),
                collection.id().value()
        );
        model.insert();
        _notifyInsert(collection);
    }

    @Override
    public boolean exists(Collection collection) {
        return _getModel(collection) != null;
    }

    @Override
    public void update(Collection collection) {
        CollectionModel model = _getModel(collection);
        if (model != null) {
            model.name = collection.name().value();
            model.type = Utils.getCollectionType(collection);
            model.collectionId = collection.id().value();
            model.update();
            _notifyUpdate(collection);
        } else {
            Log.w(TAG, "Could not find an image collection to update");
        }
    }

    @Override
    public void remove(Collection collection) {
        CollectionModel model = _getModel(collection);
        if (model != null) {
            model.delete();
            _notifyDelete(collection);
        } else {
            Log.e(TAG, "Could not find an image collection to delete");
        }
    }

    @Override
    public void replace(List<Collection> collections) {
        SQLite.delete().from(CollectionModel.class).execute();
        for (Collection collection : collections) {
            new CollectionModel(
                    collection.name().value(),
                    Utils.getCollectionType(collection),
                    collection.id().value()
            ).insert();
        }
        _notifyReplace(collections);
    }

    @NonNull
    @Override
    public List<Collection> getAll() {
        List<CollectionModel> models = SQLite.select().from(CollectionModel.class).queryList();
        List<Collection> collectionList = new ArrayList<>(models.size());
        for (CollectionModel model : models) {
            Collection collection = collectionFactory.create(
                    model.type,
                    new ServerId(model.collectionId),
                    new Name(model.name)
            );
            collectionList.add(collection);
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

    private void _notifyUpdate(Collection collection) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onUpdate(collection);
        }
    }

    @Nullable
    private CollectionModel _getModel(Collection collection) {
        return SQLite.select()
                .from(CollectionModel.class)
                .where(
                        CollectionModel_Table.collectionId.eq(collection.id().value()),
                        CollectionModel_Table.type.eq(Utils.getCollectionType(collection))
                )
                .querySingle();
    }

    private void _notifyInsert(Collection collection) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onInsert(collection);
        }
    }
    
    private void _notifyReplace(List<Collection> collections) {
        for (CollectionRepositoryListener listener : listeners) {
            listener.onReplace(collections);
        }
    }

}
