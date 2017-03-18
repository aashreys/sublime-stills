package com.aashreys.walls.persistence;

import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.persistence.collections.CollectionModel;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageModel;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by aashreys on 30/11/16.
 */

@Database(name = WallsDatabase.NAME, version = WallsDatabase.VERSION)
public class WallsDatabase {

    public static final String NAME = "walls_database";

    public static final int VERSION = 4;

    @Migration(version = 2, database = WallsDatabase.class)
    public static class Migration2 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            // Schema for FavoriteImageModel changed completely in 0.8 release, hence dropping
            // the table and re-creating it.
            database.execSQL("DROP TABLE IF EXISTS FavoriteImageModel");
            ModelAdapter favoriteImageModelAdapter = new FavoriteImageModel().getModelAdapter();
            database.execSQL(favoriteImageModelAdapter.getCreationQuery());

            // ShortUrlModel became redundant due to network caching in 0.8, hence dropping it.
            database.execSQL("DROP TABLE IF EXISTS ShortUrlModel");

            // Before 0.8 the UnsplashRecent Collection was being stored as the Discover
            // Collection. Starting 0.8, Discover collection was made into a separate collection
            // hence we need to replace the old data.
            SQLite.delete(CollectionModel.class).execute(database);
            CollectionModel discoverCollectionModel = new CollectionModel(new DiscoverCollection());
            CollectionModel favoriteCollectionModel = new CollectionModel(new FavoriteCollection());
            discoverCollectionModel.insert(database);
            favoriteCollectionModel.insert(database);

        }

        @Migration(version = 3, database = WallsDatabase.class)
        public static class Migration3 extends AlterTableMigration<FavoriteImageModel> {

            public Migration3(Class<FavoriteImageModel> table) {
                super(table);
            }

            @Override
            public void onPreMigrate() {
                super.onPreMigrate();
                addColumn(SQLiteType.TEXT, "userId");
            }
        }
    }
}
