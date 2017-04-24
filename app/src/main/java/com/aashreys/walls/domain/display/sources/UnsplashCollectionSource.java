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

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Id;
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
 * Created by aashreys on 31/01/17.
 */

@AutoFactory
public class UnsplashCollectionSource implements Source {

    private static final String TAG = UnsplashCollectionSource.class.getSimpleName();

    private final UnsplashApi unsplashApi;

    private final UnsplashPhotoResponseParser responseParser;

    private final Id collectionId;

    public UnsplashCollectionSource(
            @Provided UnsplashApi unsplashApi,
            @Provided UnsplashPhotoResponseParser responseParser,
            Id collectionId
    ) {
        this.unsplashApi = unsplashApi;
        this.responseParser = responseParser;
        this.collectionId = collectionId;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public List<Image> getImages(int fromIndex) throws IOException {
        try {
            Call<ResponseBody> call = unsplashApi.getCollectionPhotos(
                    collectionId.value(),
                    UiHelper.getPageNumber(fromIndex, UnsplashApi.ITEMS_PER_PAGE),
                    UnsplashApi.ITEMS_PER_PAGE
            );
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                return responseParser.parse(response.body().string());
            } else {
                throw new IOException("Unexpected response code " + response.code());
            }
        } catch (JSONException e) {
            throw new IOException("Image loading failed with JSONException", e);
        }
    }
}
