package com.aashreys.walls.network.apis;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by aashreys on 01/03/17.
 */

public interface UrlShortenerApi {

    String ENDPOINT = "https://www.googleapis.com/urlshortener/v1/";

    String RESPONSE_CACHE_HEADER = "max-age=3600";

    @Headers({
            "Cache-Control: max-age=3600",
            "Content-Type: application/json"
    })
    @POST("url")
    Call<ResponseBody> shorten(@Body RequestBody requestBody);

}
