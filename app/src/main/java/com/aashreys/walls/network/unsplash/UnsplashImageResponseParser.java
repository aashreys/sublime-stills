package com.aashreys.walls.network.unsplash;

import android.annotation.SuppressLint;
import android.util.Log;

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Coordinates;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Color;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.utils.LogWrapper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aashreys on 07/06/17.
 */

public class UnsplashImageResponseParser implements JsonDeserializer<Image> {

    private static final String TAG = UnsplashImageResponseParser.class.getSimpleName();

    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat DATE_PARSER
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Image deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context
    ) throws JsonParseException {
        JsonObject response = json.getAsJsonObject();
        User user = createUser(response);
        Resolution resolution = createResolution(response);
        Color backgroundColor = createBackgroundColor(response);
        Date createdAt = createCreatedAtDate(response);
        Exif exif = createExif(response);
        Location location = createLocation(response);
        Id id = new Id(response.get("id").getAsString());
        Url rawImageUrl = new Url(response.getAsJsonObject("urls").get("raw").getAsString());
        Url imageShareUrl = new Url(response.getAsJsonObject("links").get("html").getAsString());
        try {
            return new UnsplashImage(
                    id,
                    rawImageUrl,
                    imageShareUrl,
                    null,
                    createdAt,
                    user,
                    location,
                    exif,
                    resolution,
                    backgroundColor
            );
        } catch (InstantiationException e) {
            throw new JsonParseException("Unable to create unsplash image", e);
        }
    }

    private Color createBackgroundColor(JsonObject response) {
        Color backgroundColor = null;
        if (response.has("color") && !response.get("color").isJsonNull()) {
            String hexColor = response.get("color").getAsString();
            if (hexColor != null && !hexColor.isEmpty() && !hexColor.equals("null")) {
                backgroundColor = new Color(parseHexColor(hexColor));
            }
        }
        return backgroundColor;
    }

    private Resolution createResolution(JsonObject response) {
        Resolution resolution = null;
        if (response.has("width") && !response.get("width").isJsonNull() &&
                response.has("height") && !response.get("height").isJsonNull()) {
            int width = response.get("width").getAsInt();
            int height = response.get("height").getAsInt();
            if (width > 0 && height > 0) {
                try {
                    resolution = new Resolution(new Pixel(width), new Pixel(height));
                } catch (InstantiationException e) {
                    Log.w(TAG, "Unable to create resolution", e);
                }
            }
        }
        return resolution;
    }

    private User createUser(JsonObject response) {
        User user = null;
        if (response.has("user") && !response.get("user").isJsonNull()) {
            JsonObject userJson = response.getAsJsonObject("user");
            try {
                Id id = null;
                Name name = null;
                Url profileUrl = null, portfolioUrl = null;

                if (userJson.has("id") && !userJson.get("id").isJsonNull()) {
                    id = new Id(userJson.get("id").getAsString());
                }

                if (userJson.has("name") && !userJson.get("name").isJsonNull()) {
                    name = new Name(userJson.get("name").getAsString());
                }

                if (userJson.has("portfolio_url") && !userJson.get("portfolio_url").isJsonNull()) {
                    portfolioUrl = new Url(userJson.get("portfolio_url").getAsString());
                }

                if (userJson.has("links") && !userJson.get("links").isJsonNull()) {
                    JsonObject userLinks = userJson.get("links").getAsJsonObject();
                    if (userLinks.has("html") && !userLinks.get("html").isJsonNull()) {
                        profileUrl = new Url(userLinks.get("html").getAsString());
                    }
                }
                user = new User(id, name, profileUrl, portfolioUrl);
            } catch (InstantiationException e) {
                Log.w(TAG, "Unable to create user", e);
            }
        }
        return user;
    }

    private int parseHexColor(String colorString) {
        // Use a long to avoid rollovers on #ffXXXXXX
        long color = Long.parseLong(colorString.substring(1), 16);
        if (colorString.length() == 7) {
            // Set the alpha value
            color |= 0x00000000ff000000;
        } else if (colorString.length() != 9) {
            color = 0;
        }
        return (int)color;
    }

    private Date createCreatedAtDate(JsonObject response) {
        Date createdAt = null;
        if (response.has("created_at") && !response.get("created_at").isJsonNull()) {
            try {
                createdAt = DATE_PARSER.parse(response.get("created_at").getAsString());
            } catch (ParseException ignored) {
            }
        }
        return createdAt;
    }

    private Exif createExif(JsonObject response) {
        Exif exif = null;
        if (response.has("exif") && !response.get("exif").isJsonNull()) {
            JsonObject exifJson = response.get("exif").getAsJsonObject();
            try {
                exif = new Exif(
                        createCamera(exifJson),
                        createExposureTime(exifJson),
                        createAperture(exifJson),
                        createFocalLength(exifJson),
                        createIso(exifJson)
                );
            } catch (Exception e) {
                LogWrapper.e(TAG, "", e);
            }
        }
        return exif;
    }

    private Name createCamera(JsonObject exifJson) {
        Name camera = null;
        if (exifJson.has("model") && !exifJson.get("model").isJsonNull()) {
            camera = new Name(exifJson.get("model").getAsString());
        }
        return camera;
    }

    private Name createExposureTime(JsonObject exifJson) {
        Name exposureTime = null;
        if (exifJson.has("exposure_time") && !exifJson.get("exposure_time").isJsonNull()) {
            exposureTime = new Name(getFormattedExposureTime(exifJson.get("exposure_time")
                    .getAsString()));
        }
        return exposureTime;
    }

    private Name createAperture(JsonObject exifJson) {
        Name aperture = null;
        if (exifJson.has("aperture") && !exifJson.get("aperture").isJsonNull()) {
            aperture = new Name(getFormattedAperture(exifJson.get("aperture").getAsString()));
        }
        return aperture;
    }

    private Name createFocalLength(JsonObject exifJson) {
        Name focalLength = null;
        if (exifJson.has("focal_length") && !exifJson.get("focal_length").isJsonNull()) {
            focalLength = new Name(getFormattedFocalLength(exifJson.get("focal_length")
                    .getAsString()));
        }
        return focalLength;
    }

    private Name createIso(JsonObject exifJson) {
        Name iso = null;
        if (exifJson.has("iso") && !exifJson.get("iso").isJsonNull()) {
            iso = new Name(String.valueOf(exifJson.get("iso").getAsInt()));
        }
        return iso;
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

    private Location createLocation(JsonObject response) {
        Location location = null;
        if (response.has("location") && !response.get("location").isJsonNull()) {
            JsonObject locationJson = response.get("location").getAsJsonObject();
            Name locationName = createLocationName(locationJson);
            Coordinates coordinates = createCoordinates(locationJson);
            try {
                location = new Location(
                        locationName,
                        coordinates
                );
            } catch (InstantiationException e) {
                LogWrapper.w(TAG, "Could not parse location data", e);
            }

        }
        return location;
    }

    private Coordinates createCoordinates(JsonObject locationJson) {
        Coordinates coordinates = null;
        try {
            if (locationJson.has("position") && !locationJson.get("position").isJsonNull()) {
                JsonObject positionJson = locationJson.get("position").getAsJsonObject();
                double latitude = positionJson.get("latitude").getAsDouble();
                double longitude = positionJson.get("longitude").getAsDouble();
                coordinates = new Coordinates(latitude, longitude);
            }
        } catch (Exception e) {
            LogWrapper.w(TAG, "Could not parse coordinates", e);
        }
        return coordinates;
    }

    private Name createLocationName(JsonObject locationJson) {
        Name locationName = null;
        if (locationJson.has("title") && !locationJson.get("title").isJsonNull()) {
            locationName = new Name(locationJson.get("title").getAsString());
        } else {
            locationName = createLocationNameFromCityAndCountry(locationJson);
        }
        return locationName;
    }

    private Name createLocationNameFromCityAndCountry(JsonObject locationJson) {
        Name locationName = null;
        String city = null, country = null;
        if (locationJson.has("city") && !locationJson.get("city").isJsonNull()) {
            city = locationJson.get("city").getAsString();
        }
        if (locationJson.has("country") && !locationJson.get("country").isJsonNull()) {
            country = locationJson.get("country").getAsString();
        }
        String formattedLocationName = getFormattedLocationName(city, country);
        if (formattedLocationName != null) {
            locationName = new Name(formattedLocationName);
        }
        return locationName;
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
                    locationName = locationName.concat(", ").concat(country);
                }
            } else {
                locationName = country;
            }
        }
        return locationName;
    }

}
