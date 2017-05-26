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

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Color;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.utils.ColorParser;
import com.aashreys.walls.utils.JSONUtils;
import com.aashreys.walls.utils.LogWrapper;

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

    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat DATE_PARSER
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final ColorParser colorParser;

    @Inject
    public UnsplashPhotoResponseParser(ColorParser colorParser) {
        this.colorParser = colorParser;
    }

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
            User user = createUser(response.getJSONObject("user"));
            Resolution resolution = createResolution(response);
            Color backgroundColor = createBackgroundColor(response);
            return new UnsplashImage(
                    new Id(JSONUtils.getString(response, "id")),
                    new Url(JSONUtils.getString(response.getJSONObject("urls"), "raw")),
                    new Url(JSONUtils.getString(response.getJSONObject("links"), "html")),
                    null,
                    DATE_PARSER.parse(JSONUtils.getString(response, "created_at")),
                    user,
                    null,
                    null,
                    resolution,
                    backgroundColor
            );
        } catch (InstantiationException |ParseException | JSONException e) {
            LogWrapper.e(TAG, "Unable to parse image response", e);
            return null;
        }
    }

    private Color createBackgroundColor(JSONObject response) {
        String hexColor = JSONUtils.optString(response, "color", null);
        Color backgroundColor = null;
        if (hexColor != null) {
            backgroundColor = new Color(colorParser.fromHex(hexColor));
        }
        return backgroundColor;
    }

    private Resolution createResolution(JSONObject unsplashResponse) {
        Resolution resolution = null;
        int width = unsplashResponse.optInt("width", 0);
        int height = unsplashResponse.optInt("height", 0);
        if (width > 0 && height > 0) {
            try {
                resolution = new Resolution(new Pixel(width), new Pixel(height));
            } catch (InstantiationException e) {
                Log.w(TAG, "Unable to create resolution", e);
            }
        }
        return resolution;
    }

    private User createUser(JSONObject userJson) {
        User user = null;
        String id = JSONUtils.optString(userJson, "id", null);
        if (id != null) {
            try {
                String name = JSONUtils.optString(userJson, "name", null);
                String profile = JSONUtils.optString(userJson.optJSONObject("links"), "html", null);
                String portfolio = JSONUtils.optString(userJson, "portfolio_url", null);
                user = new User(new Id(id), new Name(name), new Url(profile), new Url(portfolio));
            } catch (InstantiationException e) {
                Log.w(TAG, "Unable to create user", e);
            }
        }
        return user;
    }
}
