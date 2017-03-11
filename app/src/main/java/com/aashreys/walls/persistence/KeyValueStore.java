package com.aashreys.walls.persistence;

/**
 * Created by aashreys on 11/03/17.
 */

public interface KeyValueStore {

    void putString(String key, String value);

    String getString(String key, String defaultValue);

    void putBoolean(String key, boolean bool);

    boolean getBoolean(String key, boolean defaultValue);

}
