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

package com.aashreys.walls.network;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.network.unsplash.UnsplashApi;
import com.aashreys.walls.network.unsplash.UnsplashImageResponseParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A class for creating implementations of Retrofit api interfaces.
 * Created by aashreys on 04/03/17.
 */

public class ApiFactory {

    private static final String TAG = ApiFactory.class.getSimpleName();

    public UnsplashApi createUnsplashApi(OkHttpClient client, String baseUrl) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Image.class, new UnsplashImageResponseParser())
                .create();
        return new Retrofit.Builder()
                .client(client.newBuilder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                // Add api headers
                                Request requestWithHeaders = originalRequest.newBuilder()
                                        .addHeader(
                                                "Authorization",
                                                "Client-ID " +
                                                        SafeApi.decrypt(BuildConfig
                                                                .UNSPLASH_API_KEY)
                                        )
                                        .addHeader("Accept-Version", UnsplashApi.API_VERSION)
                                        .build();
                                return chain.proceed(requestWithHeaders);
                            }
                        })
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();
                                Response response = chain.proceed(request);
                                String requestUrl = request.url().toString();
                                String header = "public, " + UnsplashApi.GENERAL_CACHE_DURATION;
                                if (requestUrl.contains("photos/")) {
                                    header = "public, " + UnsplashApi.PHOTO_INFO_CACHE_DURATION;
                                }
                                if (requestUrl.contains("/collections/featured")) {
                                    header = "public, " +
                                            UnsplashApi.FEATURED_COLLECTION_CACHE_DURATION;
                                }
                                return response.newBuilder()
                                        .header("Cache-Control", header)
                                        .build();
                            }
                        })
                        .build())
                .validateEagerly(true)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UnsplashApi.class);
    }

}
