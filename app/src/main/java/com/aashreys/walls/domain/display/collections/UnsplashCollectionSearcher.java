package com.aashreys.walls.domain.display.collections;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.Config;
import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.network.UnsplashNetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 02/02/17.
 */

public class UnsplashCollectionSearcher {

    private static final String TAG = UnsplashCollectionSearcher.class.getSimpleName();

    private static final String URL = "https://api.unsplash.com/search/collections?query=%s";

    private final CollectionValidator validator;

    private final UnsplashNetworkService networkService;

    @Inject
    public UnsplashCollectionSearcher(
            CollectionValidator validator,
            UnsplashNetworkService networkService
    ) {
        this.validator = validator;
        this.networkService = networkService;
    }

    @NonNull
    @WorkerThread
    public List<Collection> search(String searchString) {
        List<Collection> collections = new ArrayList<>();
        if (searchString != null) {
            try {
                String response = networkService.get(
                        String.format(URL, searchString),
                        SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                        Config.Unsplash.API_VERSION
                );
                if (response != null) {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        UnsplashCollection c = new UnsplashCollection(
                                new ServerId(String.valueOf(jsonObject.getLong("id"))),
                                new Name(jsonObject.getString("title"))
                        );
                        if (validator.isValid(c)) {
                            collections.add(c);
                        } else {
                            LogWrapper.d(TAG, "Skipped adding collections since it is not valid");
                        }
                    }
                }
            } catch (JSONException e) {
                LogWrapper.e(TAG, "Error while parsing collection response", e);
            }
        }
        return collections;
    }
}
