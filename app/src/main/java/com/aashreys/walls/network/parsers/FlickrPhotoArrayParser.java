package com.aashreys.walls.network.parsers;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.FlickrImage;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 02/03/17.
 */

public class FlickrPhotoArrayParser {

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
        FlickrImage image = new FlickrImage(
                new Id(photoJson.getString("id")),
                new Id(photoJson.getString("owner")),
                new Id(photoJson.getString("server")),
                new Id(String.valueOf(photoJson.getInt("farm"))),
                new Id(photoJson.getString("secret"))
        );
        String title = photoJson.optString("title");
        if (title != null && title.isEmpty()) {
            image.getProperties().title = new Name(title);
        }
        return image;
    }

}
