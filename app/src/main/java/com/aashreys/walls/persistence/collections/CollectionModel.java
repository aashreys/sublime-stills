package com.aashreys.walls.persistence.collections;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.WallsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by aashreys on 31/01/17.
 */

@Table(database = WallsDatabase.class)
public class CollectionModel extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    @Collection.Type
    String type;

    @Column
    String collectionId;

    CollectionModel() {}

    CollectionModel(String name, @Collection.Type String type, String collectionId) {
        this.name = name;
        this.type = type;
        this.collectionId = collectionId;
    }
}
