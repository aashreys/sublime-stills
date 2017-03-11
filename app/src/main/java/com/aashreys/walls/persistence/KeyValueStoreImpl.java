package com.aashreys.walls.persistence;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by aashreys on 11/03/17.
 */

public class KeyValueStoreImpl implements KeyValueStore {

    private final SharedPreferences sharedPreferences;

    @Inject
    public KeyValueStoreImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public void putBoolean(String key, boolean bool) {
        sharedPreferences.edit().putBoolean(key, bool).apply();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
