package com.aashreys.walls.network.parsers;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.values.Name;

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

    public void parse(String response, UnsplashImage image) {
        Exif exif = new Exif();
        try {
            JSONObject responseJson = new JSONObject(response);
            JSONObject exifJson = responseJson.getJSONObject("exif");

            String camera = exifJson.optString("model", null);
            if (camera != null && !camera.equals("null")) {
                exif.camera = new Name(camera);
            }

            String exposureTime = exifJson.optString("exposure_time", null);
            if (exposureTime != null && !exposureTime.equals("null")) {
                exif.exposureTime = new Name(getFormattedExposureTime(exposureTime));
            }

            String aperture = exifJson.optString("aperture", null);
            if (aperture != null && !aperture.equals("null")) {
                exif.aperture = new Name(getFormattedAperture(aperture));
            }

            String focalLength = exifJson.optString("focal_length", null);
            if (focalLength != null && !focalLength.equals("null")) {
                exif.focalLength = new Name(getFormattedFocalLength(focalLength));
            }

            int iso = exifJson.optInt("iso", 0);
            if (iso != 0) {
                exif.iso = new Name(String.valueOf(iso));
            }
            image.setExif(exif);

            JSONObject locationJson = responseJson.getJSONObject("location");
            String city = locationJson.getString("city");
            String country = locationJson.getString("country");
            JSONObject positionJson = locationJson.getJSONObject("position");
            double longitude = positionJson.getDouble("longitude");
            double latitude = positionJson.getDouble("latitude");


            String locationString = null;
            if (!city.equals("null")) {
                locationString = city;
            }
            if (locationString != null) {
                if (!country.equals("null")) {
                    locationString = locationString.concat(", ").concat(country);
                }
            } else {
                if (!country.equals("null")) {
                    locationString = country;
                }
            }

            image.setLocation(
                    new Location(
                            new Name(locationString),
                            longitude,
                            latitude
                    )
            );
        } catch (JSONException e) {
            LogWrapper.e(TAG, "Unable to parse unsplash photo info", e);
        }
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
