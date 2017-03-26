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

import com.aashreys.walls.utils.LogWrapper;
import com.aashreys.walls.domain.display.images.FlickrImage;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 02/03/17.
 */

public class FlickrPhotoArrayParser {

    private final static SimpleDateFormat DATE_PARSER
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String TAG = FlickrPhotoArrayParser.class.getSimpleName();

    @Inject
    public FlickrPhotoArrayParser() {}

    public List<Image> parse(JSONArray jsonArray) {
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject photoJson = jsonArray.getJSONObject(i);
                FlickrImage image = _parse(photoJson);
                imageList.add(image);
            } catch (JSONException e) {
                LogWrapper.e(TAG, "Unable to parse response", e);
            }
        }
        return imageList;
    }

    private FlickrImage _parse(JSONObject photoJson) throws JSONException {
        Name userName = null;
        Date createdAt = null;
        Location location = null;
        try {
            userName = new Name(photoJson.getString("ownername"));
            createdAt = DATE_PARSER.parse(photoJson.getString("datetaken"));
            double longitude = photoJson.getDouble("longitude");
            double latitude = photoJson.getDouble("latitude");
            location = new Location(null, longitude, latitude);
        } catch (JSONException | ParseException ignored) {}

        FlickrImage image = new FlickrImage(
                new Id(photoJson.getString("id")),
                new Id(photoJson.getString("owner")),
                new Id(photoJson.getString("server")),
                new Id(String.valueOf(photoJson.getInt("farm"))),
                new Id(photoJson.getString("secret")),
                new Name(photoJson.getString("title")),
                userName,
                createdAt,
                location
        );
        return image;
    }
}
