package com.aashreys.walls.persistence;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by aashreys on 30/11/16.
 */

@Database(name = WallsDatabase.NAME, version = WallsDatabase.VERSION)
public class WallsDatabase {

    public static final String NAME = "walls_database";

    public static final int VERSION = 1;

}
