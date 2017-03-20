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
