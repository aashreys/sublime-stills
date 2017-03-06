package com.aashreys.walls.network;

import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.apis.UrlShortenerApi;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aashreys on 03/12/16.
 */

public class UrlShortenerImpl implements UrlShortener {

    private static final String TAG = UrlShortenerImpl.class.getSimpleName();

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final String POST_STRING_TEMPLATE = "{\"longUrl\": \"%s\"}";

    private final ShortUrlRepository shortUrlRepository;

    private final UrlShortenerApi urlShortenerApi;

    public UrlShortenerImpl(
            ShortUrlRepository shortUrlRepository,
            UrlShortenerApi urlShortenerApi
    ) {
        this.shortUrlRepository = shortUrlRepository;
        this.urlShortenerApi = urlShortenerApi;
    }

    @Override
    public void shorten(final Url longUrl, final Listener listener) {
        if (longUrl != null && longUrl.isValid()) {
            Url cachedUrl = shortUrlRepository.get(longUrl);
            if (cachedUrl != null) {
                listener.onComplete(cachedUrl);
            } else {
                Call<ResponseBody> call = urlShortenerApi.shorten(
                        RequestBody.create(MEDIA_TYPE, getPostString(longUrl))
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                Url shortUrl = getShortUrlFromResponse(response);
                                shortUrlRepository.save(longUrl, shortUrl);
                                listener.onComplete(shortUrl);
                            } catch (JSONException | IOException e) {
                                listener.onError(new UrlShortenerException(e));
                            }
                        } else {
                            listener.onError(new UrlShortenerException(new IOException(
                                    "Unexpected error code " + response.code())));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        listener.onError(new UrlShortenerException(t));
                    }
                });
            }
        } else {
            listener.onError(new UrlShortenerException("Url is invalid or null"));
        }
    }

    private String getPostString(Url url) {
        return String.format(POST_STRING_TEMPLATE, url.value());
    }

    private Url getShortUrlFromResponse(Response<ResponseBody> response) throws
            JSONException,
            IOException {
        JSONObject json = new JSONObject(response.body().string());
        return new Url(json.getString("id"));
    }
}
