package com.aashreys.walls.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aashreys on 03/12/16.
 */

public class JSONUtils {

    public static String getString(JSONObject object, String key) throws JSONException {
        String value = object.getString(key);
        return (value != null && !value.equals("null")) ? value : null;
    }

    public static String optString(JSONObject object, String key, String fallback) {
        String value = object.optString(key, fallback);
        return (value != null && !value.equals("null")) ? value : null;
    }

}
