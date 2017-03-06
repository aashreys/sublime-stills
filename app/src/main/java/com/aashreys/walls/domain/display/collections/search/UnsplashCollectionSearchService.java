package com.aashreys.walls.domain.display.collections.search;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

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
    public List<Collection> search(String searchString) {
        try {
            Call<ResponseBody> call = unsplashApi.searchCollections(searchString, 0, 20);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                List<Collection> collectionList = new ArrayList<>();
                JSONArray jsonArray = new JSONObject(response.body().string()).getJSONArray(
                        "results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    UnsplashCollection c = new UnsplashCollection(
                            new Id(String.valueOf(jsonObject.getLong("id"))),
                            new Name(jsonObject.getString("title"))
                    );
                    if (c.getId().isValid() && c.getName().isValid()) {
                        collectionList.add(c);
                    }
                }
                return collectionList;
            } else {
                throw new IOException("Unexpected error code" + response.code());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
