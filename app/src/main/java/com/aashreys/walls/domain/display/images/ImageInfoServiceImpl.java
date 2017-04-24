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

package com.aashreys.walls.domain.display.images;

import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoInfoParser;
import com.aashreys.walls.utils.LogWrapper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aashreys on 05/03/17.
 */

public class ImageInfoServiceImpl implements ImageInfoService {

    private static final String TAG = ImageInfoServiceImpl.class.getSimpleName();

    private final UnsplashApi unsplashApi;

    private final UnsplashPhotoInfoParser unsplashPhotoInfoParser;

    public ImageInfoServiceImpl(
            UnsplashApi unsplashApi,
            UnsplashPhotoInfoParser unsplashPhotoInfoParser
    ) {
        this.unsplashApi = unsplashApi;
        this.unsplashPhotoInfoParser = unsplashPhotoInfoParser;
    }

    @Override
    public void addInfo(Image image, Listener listener) {
        switch (image.getType()) {
            case Image.Type.UNSPLASH:
                addUnsplashInfo((UnsplashImage) image, listener);
                break;

            default:
                listener.onComplete(image);
                break;
        }
    }

    private void addUnsplashInfo(final UnsplashImage image, final Listener listener) {
        Call<ResponseBody> call = unsplashApi.getPhoto(image.getId().value());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        image.setExif(unsplashPhotoInfoParser.getExif(responseBody));
                        image.setLocation(unsplashPhotoInfoParser.getLocation(responseBody));
                    } catch (IOException e) {
                        LogWrapper.e(
                                TAG,
                                "Could not set Unsplash info, request failed with code " +
                                        response.code(), e
                        );
                    }
                } else {
                    LogWrapper.e(
                            TAG,
                            "Could not set Unsplash info, request failed with code " +
                                    response.code()
                    );
                }
                listener.onComplete(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onComplete(image);
            }
        });
    }
}
