package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;

import com.aashreys.walls.Config;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.apis.FlickrApi;
import com.aashreys.walls.network.parsers.FlickrPhotoArrayParser;
import com.aashreys.walls.network.parsers.Utils;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by aashreys on 02/03/17.
 */

@AutoFactory
public class FlickrTagSource implements Source {

    private final Name tag;

    private final FlickrApi flickrApi;

    private final FlickrPhotoArrayParser parser;

    private static final String TAG = FlickrTagSource.class.getSimpleName();

    public FlickrTagSource(
            Name tag,
            @Provided FlickrApi flickrApi,
            @Provided FlickrPhotoArrayParser parser
    ) {
        this.tag = tag;
        this.flickrApi = flickrApi;
        this.parser = parser;
    }

    @NonNull
    @Override
    public List<Image> getImages(int fromIndex) {
        Call<ResponseBody> call = flickrApi.searchPhotos(
                tag.value(),
                FlickrApi.PHOTO_EXTRAS,
                UiHelper.getPageNumber(fromIndex, Config.Flickr.ITEMS_PER_PAGE),
                Config.Flickr.ITEMS_PER_PAGE
        );
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                responseString = Utils.removeFlickrResponseBrackets(responseString);
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonPhotoArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
                return parser.parse(jsonPhotoArray);
            } else {
                throw new IOException("Unexpected error code " + response.code());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
