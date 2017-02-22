package com.aashreys.walls.network;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aashreys on 09/02/17.
 */

public class UnsplashNetworkServiceImpl implements UnsplashNetworkService {

    private static final String TAG = UnsplashNetworkServiceImpl.class.getSimpleName();

    private final OkHttpClient httpClient;

    public UnsplashNetworkServiceImpl(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Nullable
    @Override
    public String get(String url, String appId, String apiVersion) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Client-ID " + appId)
                .addHeader("Accept-Version", apiVersion)
                .build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to fetch response", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
