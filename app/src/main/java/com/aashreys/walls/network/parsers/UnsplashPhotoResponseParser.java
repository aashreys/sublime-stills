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

package com.aashreys.walls.network.parsers;

import android.support.annotation.NonNull;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 04/02/17.
 */
public class UnsplashPhotoResponseParser {

    private static final String TAG = UnsplashPhotoResponseParser.class.getSimpleName();

    private final static SimpleDateFormat DATE_PARSER
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Inject
    public UnsplashPhotoResponseParser() {}

    @NonNull
    public List<Image> parse(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Image image = _parse(jsonArray.getJSONObject(i));
            if (image != null) {
                imageList.add(image);
            } else {
                LogWrapper.d(TAG, "Skipping adding image to list as it is invalid");
            }
        }
        return imageList;
    }

    private Image _parse(JSONObject response) {
        try {
            JSONObject user = response.getJSONObject("user");
            return new UnsplashImage(
                    new Id(JSONUtils.getString(response, "id")),
                    new Pixel(response.getInt("width")),
                    new Pixel(response.getInt("height")),
                    DATE_PARSER.parse(JSONUtils.getString(response, "created_at")),
                    new Id(user.getString("id")),
                    new Name(JSONUtils.getString(user, "name")),
                    new Url(JSONUtils.getString(user.getJSONObject("links"), "html")),
                    new Url(JSONUtils.optString(user, "portfolio_url", null)),
                    new Url(JSONUtils.getString(response.getJSONObject("urls"), "raw")),
                    new Url(JSONUtils.getString(response.getJSONObject("links"), "html"))
            );
        } catch (ParseException | JSONException e) {
            LogWrapper.e(TAG, "Unable to parse image response", e);
            return null;
        }
    }
}
