package com.aashreys.walls.ui.helpers;

import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

/**
 * Created by aashreys on 11/03/17.
 */

public class StartupHelper {

    private static final String KEY_IS_FIRST_START = "key_is_first_start";

    private final KeyValueStore keyValueStore;

    @Inject
    public StartupHelper(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    public boolean isFirstStart() {
        return keyValueStore.getBoolean(KEY_IS_FIRST_START, true);
    }

    public void onFirstStartCompleted() {
        // Completed first start, save state.
        keyValueStore.putBoolean(KEY_IS_FIRST_START, false);
    }

}
