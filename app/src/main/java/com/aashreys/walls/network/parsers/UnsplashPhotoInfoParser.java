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

import android.util.Log;

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.metadata.Coordinates;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.utils.JSONUtils;
import com.aashreys.walls.utils.LogWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by aashreys on 18/03/17.
 */

public class UnsplashPhotoInfoParser {

    private static final String TAG = UnsplashPhotoInfoParser.class.getSimpleName();

    @Inject
    public UnsplashPhotoInfoParser() {}

    public Location getLocation(String responseBody) {
        Location location = null;
        try {
            JSONObject response = new JSONObject(responseBody);
            JSONObject locationJson = response.getJSONObject("location");
            location = createLocation(locationJson);
        } catch (JSONException e) {
            Log.e(TAG, "No location information in response", e);
        }
        return location;
    }

    public Exif getExif(String responseBody) {
        Exif exif = null;
        try {
            JSONObject response = new JSONObject(responseBody);
            JSONObject exifJson = response.getJSONObject("exif");
            exif = createExif(exifJson);
        } catch (JSONException e) {
            Log.e(TAG, "No exif information in response", e);
        }
        return exif;
    }

    private Location createLocation(JSONObject locationJson) {
        String city = JSONUtils.optString(locationJson, "city", null);
        String country = JSONUtils.optString(locationJson, "country", null);
        JSONObject positionJson = locationJson.optJSONObject("position");
        double longitude = 0, latitude = 0;
        if (positionJson != null) {
            longitude = positionJson.optDouble("longitude", 0);
            latitude = positionJson.optDouble("latitude", 0);
        }
        String locationName = getFormattedLocationName(city, country);

        Coordinates coordinates = null;
        try {
            coordinates = new Coordinates(latitude, longitude);
        } catch (InstantiationException ignored) {}

        Location location = null;
        try {
            location = new Location(new Name(locationName), coordinates);
        } catch (InstantiationException e) {
            LogWrapper.e(TAG, "", e);
        }
        return location;
    }

    private String getFormattedLocationName(
            String city,
            String country
    ) {
        String locationName = null;
        if (city != null || country != null) {
            if (city != null) {
                locationName = city;
                if (country != null) {
                    locationName.concat(", ").concat(country);
                }
            } else {
                locationName = country;
            }
        }
        return locationName;
    }

    private Exif createExif(JSONObject exifJson) {
        Exif exif = null;
        if (exifJson != null) {
            try {
                exif = new Exif(
                        createCamera(exifJson),
                        createExposureTime(exifJson),
                        createAperture(exifJson),
                        createFocalLength(exifJson),
                        createIso(exifJson)
                );
            } catch (InstantiationException e) {
                LogWrapper.e(TAG, "", e);
            }
        }
        return exif;
    }

    private Name createCamera(JSONObject exifJson) {
        String camera = JSONUtils.optString(exifJson, "model", null);
        if (camera != null) {
            return new Name(camera);
        }
        return null;
    }

    private Name createExposureTime(JSONObject exifJson) {
        String exposureTime = JSONUtils.optString(exifJson, "exposure_time", null);
        if (exposureTime != null) {
            new Name(getFormattedExposureTime(exposureTime));
        }
        return null;
    }

    private Name createAperture(JSONObject exifJson) {
        String aperture = JSONUtils.optString(exifJson, "aperture", null);
        if (aperture != null) {
            new Name(getFormattedAperture(aperture));
        }
        return null;
    }

    private Name createFocalLength(JSONObject exifJson) {
        String focalLength = JSONUtils.optString(exifJson, "focal_length", null);
        if (focalLength != null) {
            new Name(getFormattedFocalLength(focalLength));
        }
        return null;
    }

    private Name createIso(JSONObject exifJson) {
        int iso = exifJson.optInt("iso", 0);
        if (iso != 0) {
            new Name(String.valueOf(iso));
        }
        return null;
    }

    private String getFormattedExposureTime(String rawExposureTime) {
        return rawExposureTime.concat(" sec");
    }


    private String getFormattedAperture(String rawAperture) {
        return "f/".concat(rawAperture);
    }

    private String getFormattedFocalLength(String rawFocalLength) {
        return rawFocalLength.concat(" mm");
    }

}
