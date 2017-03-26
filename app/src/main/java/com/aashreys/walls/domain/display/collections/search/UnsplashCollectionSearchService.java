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
import android.support.annotation.WorkerThread;

import com.aashreys.walls.utils.LogWrapper;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.apis.UnsplashApi;

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
 * Created by aashreys on 02/02/17.
 */

public class UnsplashCollectionSearchService implements CollectionSearchService {

    private static final String TAG = UnsplashCollectionSearchService.class.getSimpleName();

    private final UnsplashApi unsplashApi;

    @Inject
    public UnsplashCollectionSearchService(UnsplashApi unsplashApi) {
        this.unsplashApi = unsplashApi;
    }

    @NonNull
    @WorkerThread
    public List<Collection> search(String searchString, int minCollectionSize) {
        try {
            Call<ResponseBody> call = unsplashApi.searchCollections(searchString, 0, 30);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                JSONArray collectionJsonArray = new JSONObject(responseString).getJSONArray(
                        "results");
                return parseResponse(collectionJsonArray, minCollectionSize);
            } else {
                throw new IOException("Unexpected error code" + response.code());
            }
        } catch (IOException | JSONException e) {
            LogWrapper.e(TAG, "Failed to search for collections", e);
            return new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public List<Collection> getFeatured() {
        try {
            Call<ResponseBody> call = unsplashApi.getFeaturedCollections(0, 30);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                JSONArray collectionJsonArray = new JSONArray(responseString);
                return parseResponse(collectionJsonArray, 0);
            } else {
                throw new IOException("Unexpected error code" + response.code());
            }
        } catch (JSONException | IOException e) {
            LogWrapper.e(TAG, "Failed to get featured collections", e);
            return new ArrayList<>();
        }
    }

    private List<Collection> parseResponse(JSONArray collectionJsonArray, int minPhotos) throws
            JSONException {
        List<Collection> collectionList = new ArrayList<>();
        for (int i = 0; i < collectionJsonArray.length(); i++) {
            JSONObject jsonObject = collectionJsonArray.getJSONObject(i);
            int size = jsonObject.getInt("total_photos");
            if (size >= minPhotos) {
                UnsplashCollection c = new UnsplashCollection(
                        new Id(String.valueOf(jsonObject.getLong("id"))),
                        new Name(jsonObject.getString("title"))
                );
                if (c.getId().isValid() && c.getName().isValid()) {
                    collectionList.add(c);
                }
            }
        }
        return collectionList;
    }
}
