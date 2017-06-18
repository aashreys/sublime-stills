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

import com.aashreys.walls.application.helpers.UiHelper;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.network.unsplash.UnsplashApi;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by aashreys on 21/11/16.
 */

@AutoFactory
public class UnsplashRecentSource implements Source {

    private static final String TAG = UnsplashRecentSource.class.getSimpleName();

    private UnsplashApi unsplashApi;

    public UnsplashRecentSource(@Provided UnsplashApi unsplashApi) {
        this.unsplashApi = unsplashApi;
    }

    @NonNull
    @WorkerThread
    @Override
    public Single<List<Image>> getImages(int fromIndex) {
        return unsplashApi.getRecentPhotos(
                UiHelper.getPageNumber(fromIndex, UnsplashApi.ITEMS_PER_PAGE),
                UnsplashApi.ITEMS_PER_PAGE
        );
    }
}
