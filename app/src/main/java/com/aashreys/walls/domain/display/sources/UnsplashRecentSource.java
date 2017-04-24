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

package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoResponseParser;
import com.aashreys.walls.application.helpers.UiHelper;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by aashreys on 21/11/16.
 */

@AutoFactory
public class UnsplashRecentSource implements Source {

    private static final String TAG = UnsplashRecentSource.class.getSimpleName();

    private UnsplashApi unsplashApi;

    private final UnsplashPhotoResponseParser responseParser;

    public UnsplashRecentSource(
            @Provided UnsplashApi unsplashApi,
            @Provided UnsplashPhotoResponseParser responseParser
    ) {
        this.unsplashApi = unsplashApi;
        this.responseParser = responseParser;
    }

    @NonNull
    @WorkerThread
    @Override
    public List<Image> getImages(int fromIndex) throws IOException {
        try {
            Call<ResponseBody> call = unsplashApi.getRecentPhotos(
                    UiHelper.getPageNumber(fromIndex, UnsplashApi.ITEMS_PER_PAGE),
                    UnsplashApi.ITEMS_PER_PAGE
            );
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                return responseParser.parse(response.body().string());
            } else {
                throw new IOException("Unexpected error code " + response.code());
            }
        } catch (JSONException e) {
            throw new IOException("Image loading failed with JSONException", e);
        }
    }
}
