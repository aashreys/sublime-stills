package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImageImpl;
import com.aashreys.walls.domain.display.images.ImageValidator;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.ServerId;
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
public class UnsplashImageResponseParser {

    private static final String TAG = UnsplashImageResponseParser.class.getSimpleName();

    private static final String serviceNameString = "Unsplash";
    private static final String serviceUrlString  = "https://unsplash.com";

    private final static SimpleDateFormat DATE_PARSER
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final ImageValidator validator;

    @Inject
    public UnsplashImageResponseParser(ImageValidator validator) {
        this.validator = validator;
    }

    @NonNull
    List<Image> parse(String response) {
        List<Image> imageList = new ArrayList<>();
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Image image = _parse(jsonArray.getJSONObject(i));
                    if (image != null && validator.isValid(image)) {
                        imageList.add(image);
                    } else {
                        LogWrapper.d(TAG, "Skipping adding image to list as it is invalid");
                    }
                }
            } catch (JSONException e) {
                LogWrapper.e(TAG, "Unable to parse response", e);
            }
        }
        return imageList;
    }

    private Image _parse(JSONObject response) {
        try {
            JSONObject user = response.getJSONObject("user");
            JSONObject imageUrls = response.getJSONObject("urls");
            return new ImageImpl(
                    new ServerId(JSONUtils.getString(response, "id")),
                    new Pixel(response.getInt("width")),
                    new Pixel(response.getInt("height")),
                    DATE_PARSER.parse(JSONUtils.getString(response, "created_at")),
                    new Name(JSONUtils.getString(user, "name")),
                    new Url(JSONUtils.getString(user.getJSONObject("links"), "html")),
                    new Url(JSONUtils.optString(user, "portfolio_url", null)),
                    new Url(JSONUtils.getString(imageUrls, "small")),
                    new Url(JSONUtils.getString(imageUrls, "regular")),
                    new Url(JSONUtils.getString(imageUrls, "full")),
                    new Name(serviceNameString),
                    new Url(serviceUrlString)
            );
        } catch (ParseException | JSONException e) {
            LogWrapper.e(TAG, "Unable to parse unsplash image response", e);
            return null;
        }
    }
}
