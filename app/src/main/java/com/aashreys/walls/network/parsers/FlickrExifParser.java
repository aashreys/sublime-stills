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

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.utils.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by aashreys on 18/03/17.
 */

public class FlickrExifParser {

    private static final String TAG = FlickrExifParser.class.getSimpleName();

    @Inject
    public FlickrExifParser() {}

    public Exif parse(String exifString) {
        Exif exif = null;
        try {
            JSONObject exifJson = new JSONObject(exifString);
            JSONArray exifJsonArray = exifJson.getJSONObject("photo").getJSONArray("exif");
            exif = createExif(exifJsonArray);
        } catch (JSONException e) {
            LogWrapper.e(TAG, "Unable to read exif for flickr image", e);
        }
        return exif;
    }

    private Exif createExif(JSONArray exifArray) {
        String camera, exposureTime, aperture, focalLength, iso;
        camera = exposureTime = aperture = focalLength = iso = null;
        for (int i = 0; i < exifArray.length(); i++) {
            try {
                JSONObject exifJson = exifArray.getJSONObject(i);
                String tag = exifJson.getString("tag");
                switch (tag) {

                    case "Model":
                        camera = getExifValue(exifJson);
                        break;

                    case "ExposureTime":
                        exposureTime = getExifValue(exifJson);
                        break;

                    case "FNumber":
                        aperture = getExifValue(exifJson);
                        break;

                    case "FocalLength":
                        focalLength = getExifValue(exifJson);
                        break;

                    case "ISO":
                        iso = getExifValue(exifJson);
                        break;
                }
            } catch (JSONException e) {
                LogWrapper.d(TAG, "Unable to read exif property", e);
            }
        }
        Exif exif = null;
        try {
            exif = new Exif(
                    camera != null ? new Name(camera) : null,
                    exposureTime != null ? new Name(exposureTime) : null,
                    aperture != null ? new Name(aperture) : null,
                    focalLength != null ? new Name(focalLength) : null,
                    iso != null ? new Name(iso) : null
            );
        } catch (InstantiationException e) {
            LogWrapper.e(TAG, "Unable to create exif while parsing flick response", e);
        }
        return exif;
    }

    private String getExifValue(JSONObject exifJson) {
        try {
            return exifJson.getJSONObject("clean").getString("_content");
        } catch (JSONException e) {
            try {
                return exifJson.getJSONObject("raw").getString("_content");
            } catch (JSONException e1) {
                return null;
            }
        }
    }
}
