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
