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

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.values.Name;

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
        Exif exif = new Exif();
        try {
            JSONObject exifJson = new JSONObject(exifString);
            JSONArray exifJsonArray = exifJson.getJSONObject("photo").getJSONArray("exif");
            readExif(exifJsonArray, exif);
        } catch (JSONException e) {
            LogWrapper.e(TAG, "Unable to read exif for flickr image", e);
        }
        return exif;
    }

    private void readExif(JSONArray exifArray, Exif exif) {
        for (int i = 0; i < exifArray.length(); i++) {
            try {
                JSONObject exifJson = exifArray.getJSONObject(i);
                String tag = exifJson.getString("tag");
                switch (tag) {

                    case "Model":
                        String camera = getExifValue(exifJson);
                        if (camera != null) {
                            exif.camera = new Name(camera);
                        }
                        break;

                    case "ExposureTime":
                        String exposureTime = getExifValue(exifJson);
                        if (exposureTime != null) {
                            exif.exposureTime = new Name(exposureTime);
                        }
                        break;

                    case "FNumber":
                        String aperture = getExifValue(exifJson);
                        if (aperture != null) {
                            exif.aperture = new Name(aperture);
                        }
                        break;

                    case "FocalLength":
                        String focalLength = getExifValue(exifJson);
                        if (focalLength != null) {
                            exif.focalLength = new Name(focalLength);
                        }
                        break;

                    case "ISO":
                        String iso = getExifValue(exifJson);
                        if (iso != null) {
                            exif.iso = new Name(iso);
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
