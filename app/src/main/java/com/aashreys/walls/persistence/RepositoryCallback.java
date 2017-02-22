package com.aashreys.walls.persistence;

/**
 * Created by aashreys on 07/12/16.
 */

public interface RepositoryCallback<T> {

    void onInsert(T object);

    void onUpdate(T object);

    void onDelete(T object);

}
