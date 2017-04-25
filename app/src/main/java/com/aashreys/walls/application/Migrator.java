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

package com.aashreys.walls.application;

import android.content.Context;

import com.aashreys.walls.application.activities.StreamActivityModel;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepositoryImpl;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepositoryImpl;
import com.aashreys.walls.utils.LogWrapper;

import javax.inject.Inject;

import io.paperdb.Paper;

/**
 * A class for managing application migration across updates.
 * <p>
 * Created by aashreys on 24/04/17.
 */

public class Migrator {

    private static final String TAG = Migrator.class.getSimpleName();

    private static final String KEY_LAST_VERSION = "migrator_key_last_version";

    private static final int CURRENT_VERSION = Version.V12;

    private final KeyValueStore keyValueStore;

    private final Context context;

    @Inject
    public Migrator(Context context, KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
        this.context = context.getApplicationContext();
    }

    private int getLastVersion() {
        return keyValueStore.getInt(KEY_LAST_VERSION, CURRENT_VERSION);
    }

    public void migrate() {
        int lastVersion = getLastVersion();
        LogWrapper.i(TAG, "Migrating from version " + lastVersion + " to " + CURRENT_VERSION);
        switch (lastVersion) {
            case Version.V11:
                migrateFromV11ToV12();
            case CURRENT_VERSION:
                break;
        }
        completeMigration();
    }

    private void completeMigration() {
        LogWrapper.i(TAG, "Migration to version " + CURRENT_VERSION + " complete");
        keyValueStore.putInt(KEY_LAST_VERSION, CURRENT_VERSION);
    }

    private void migrateFromV11ToV12() {
        // Delete old SQL database since we've switched to NoSql
        context.deleteDatabase("walls_database");
        Paper.book(FavoriteImageRepositoryImpl.BOOK_NAME).destroy();
        Paper.book(CollectionRepositoryImpl.BOOK_NAME).destroy();
        keyValueStore.putBoolean(StreamActivityModel.KEY_IS_ONBOARDING_COMPLETED, false);
    }

    private interface Version {

        int V11 = 11;

        int V12 = 12;

    }

}
