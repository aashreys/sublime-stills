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

package com.aashreys.walls.domain.display.collections.search;

import android.support.annotation.NonNull;

import com.aashreys.walls.utils.LogWrapper;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FlickrTag;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.apis.FlickrApi;
import com.aashreys.walls.utils.JSONParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by aashreys on 05/03/17.
 */

public class FlickrTagSearchService implements CollectionSearchService {

    private static final String TAG = FlickrTagSearchService.class.getSimpleName();

    private final FlickrApi flickrApi;

    @Inject
    public FlickrTagSearchService(FlickrApi flickrApi) {
        this.flickrApi = flickrApi;
    }

    // TODO: Add support for min items
    @NonNull
    @Override
    public List<Collection> search(String tag, int minCollectionSize) {
        List<Collection> tagList = new ArrayList<>();
        Call<ResponseBody> call = flickrApi.getRelatedTags(tag);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                responseString = JSONParsingUtils.removeFlickrResponseBrackets(responseString);
                JSONArray tagJsonArray = new JSONObject(responseString)
                        .getJSONObject("tags")
                        .getJSONArray("tag");

                for (int i = 0; i < tagJsonArray.length(); i++) {
                    String tagString = tagJsonArray.getJSONObject(i).getString("_content");
                    FlickrTag flickrTag = new FlickrTag(new Name(tagString));
                    tagList.add(flickrTag);
                }
                if (tagList.size() > 0) {
                    tagList.add(0, new FlickrTag(new Name(tag)));
                }
            } else {
                LogWrapper.e(
                        TAG,
                        "Could not search flickr tags. Request failed with code " + response.code()
                );
            }
        } catch (IOException | JSONException e) {
            LogWrapper.e(TAG, "Could not search flickr tags", e);
        }
        return tagList;
    }

    // TODO: Implement Flickr featured tag search
    @NonNull
    @Override
    public List<Collection> getFeatured() {
        return new ArrayList<>();
    }

}
