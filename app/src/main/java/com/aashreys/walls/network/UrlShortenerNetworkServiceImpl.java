package com.aashreys.walls.network;

import com.aashreys.walls.domain.values.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by aashreys on 10/02/17.
 */

public class UrlShortenerNetworkServiceImpl implements UrlShortenerNetworkService {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient httpClient;

    public UrlShortenerNetworkServiceImpl(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void postAsync(
            String url, String apiKey, String postData, final UrlShortener.Listener listener
    ) {
        Request request = buildRequest(url, apiKey, postData);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(new UrlShortener.UrlShortenerException(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        listener.onComplete(getShortUrlFromResponse(response));
                    } else {
                        throw new IOException("Unexpected code " + response.code());
                    }
                } catch (JSONException | IOException e) {
                    listener.onError(new UrlShortener.UrlShortenerException(e));
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            }
        });
    }

    private Url getShortUrlFromResponse(Response response) throws JSONException, IOException {
        JSONObject json = new JSONObject(response.body().string());
        return new Url(json.getString("id"));
    }

    private Request buildRequest(String url, String apiKey, String postData) {
        return new Request.Builder()
                .url(String.format(url, apiKey))
                .post(RequestBody.create(MEDIA_TYPE, postData))
                .build();
    }
}
