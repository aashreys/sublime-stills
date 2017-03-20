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
