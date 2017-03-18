package com.aashreys.walls.network.parsers;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.info.Exif;
import com.aashreys.walls.domain.display.images.info.Location;
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

    public void parse(String response, Image image) {
        Exif exif = new Exif();
        try {
            JSONObject responseJson = new JSONObject(response);
            JSONObject exifJson = responseJson.getJSONObject("exif");
            exif.camera = new Name(exifJson.optString("model", null));

            String exposureTime = exifJson.optString("exposure_time", null);
            if (exposureTime != null) {
                exif.exposureTime = new Name(getFormattedExposureTime(exposureTime));
            }

            String aperture = exifJson.optString("aperture", null);
            if (aperture != null) {
                exif.aperture = new Name(getFormattedAperture(aperture));
            }

            String focalLength = exifJson.optString("focal_length", null);
            if (focalLength != null) {
                exif.focalLength = new Name(getFormattedFocalLength(focalLength));
            }

            exif.iso = new Name(String.valueOf(exifJson.optInt("iso", 0)));
            image.getInfo().exif = exif;

            JSONObject locationJson = responseJson.getJSONObject("location");
            String city = locationJson.getString("city");
            String country = locationJson.getString("country");
            JSONObject positionJson = locationJson.getJSONObject("position");
            double longitude = positionJson.getDouble("longitude");
            double latitude = positionJson.getDouble("latitude");
            image.getInfo().location = new Location(
                    new Name(city + ", " + country),
                    longitude,
                    latitude
            );
        } catch (JSONException e) {
            LogWrapper.e(TAG, "Unable to parse unsplash photo info", e);
        }
    }

    /**
     * Returns string of formatting "0.11 sec"
     *
     * @param rawExposureTime
     * @return
     */
    private String getFormattedExposureTime(String rawExposureTime) {
        return truncateDecimalString(rawExposureTime, 2).concat(" sec");
    }

    /**
     * Returns a string of formatting "f/2.4"
     *
     * @param rawAperture
     * @return
     */
    private String getFormattedAperture(String rawAperture) {
        return "f/".concat(truncateDecimalString(rawAperture, 1));
    }

    private String getFormattedFocalLength(String rawFocalLength) {
        return rawFocalLength.concat(" mm");
    }

    /**
     * Truncates a string to a number of decimal places after the decimal point.
     *
     * @param decimalString string to truncate
     * @param decimalPlaces decimal places to keep in string
     * @return truncated string
     */
    private String truncateDecimalString(String decimalString, int decimalPlaces) {
        int decimalIndex = decimalString.indexOf('.');
        if (decimalIndex != -1) {
            int idealEndIndex = decimalIndex + decimalPlaces;
            int endIndex = idealEndIndex > decimalString.length() ?
                    decimalString.length() :
                    idealEndIndex;
            decimalString = decimalString.substring(0, endIndex);
        }
        return decimalString;
    }

}
