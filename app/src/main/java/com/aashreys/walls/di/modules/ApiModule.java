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

package com.aashreys.walls.di.modules;

import android.content.Context;

import com.aashreys.walls.di.scopes.ApplicationScoped;
import com.aashreys.walls.network.apis.ApiInstanceCreator;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.apis.UrlShortenerApi;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by aashreys on 01/03/17.
 */

@Module
public class ApiModule {

    private final OkHttpClient okHttpClient;

    private final UnsplashApi unsplashApi;

    private final UrlShortenerApi urlShortenerApi;

    private static final String CACHE_DIR = "http_response_cache";

    public ApiModule(Context context) {
        int cacheSize = 20 * 1024 * 1024; // 20 MiB
        Cache cache = new Cache(new File(context.getCacheDir(), CACHE_DIR), cacheSize);
        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        ApiInstanceCreator apiInstanceCreator = new ApiInstanceCreator();
        unsplashApi = apiInstanceCreator.createUnsplashApi(okHttpClient, UnsplashApi.ENDPOINT);
        urlShortenerApi = apiInstanceCreator.createUrlShortenerApi(
                okHttpClient,
                UrlShortenerApi.ENDPOINT
        );
    }

    @Provides
    @ApplicationScoped
    public UnsplashApi providesUnsplashApi() {
        return unsplashApi;
    }

    @Provides
    @ApplicationScoped
    public UrlShortenerApi providesUrlShortenerApi() {
        return urlShortenerApi;
    }
}
