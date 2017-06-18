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

package com.aashreys.walls.domain.display.collections.search;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.network.unsplash.UnsplashApi;
import com.aashreys.walls.network.unsplash.UnsplashCollectionResponse;
import com.aashreys.walls.network.unsplash.UnsplashCollectionSearchResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by aashreys on 02/02/17.
 */

public class UnsplashCollectionDiscoveryService implements CollectionDiscoveryService {

    private static final String TAG = UnsplashCollectionDiscoveryService.class.getSimpleName();

    private final UnsplashApi unsplashApi;

    @Inject
    UnsplashCollectionDiscoveryService(UnsplashApi unsplashApi) {
        this.unsplashApi = unsplashApi;
    }

    @NonNull
    public Single<List<Collection>> search(String searchString, final int minSize) {
        return unsplashApi.searchCollections(searchString, 0, 30)
                .map(new Function<UnsplashCollectionSearchResponse, List<Collection>>() {
                    @Override
                    public List<Collection> apply(
                            @io.reactivex.annotations.NonNull
                                    UnsplashCollectionSearchResponse
                                    unsplashCollectionSearchResponse
                    ) throws Exception {
                        List<Collection> collectionList = new ArrayList<Collection>();
                        for (UnsplashCollectionResponse collectionResponse :
                                unsplashCollectionSearchResponse.getCollectionResponseList()) {
                            if (collectionResponse.getTotalPhotos() >= minSize) {
                                collectionList.add(collectionResponse.toCollection());
                            }
                        }
                        return collectionList;
                    }
                });
    }

    @NonNull
    @Override
    public Single<List<Collection>> getFeatured() {
        return unsplashApi.getFeaturedCollections(0, 30).map(new Function<List
                <UnsplashCollectionResponse>, List<Collection>>() {
            @Override
            public List<Collection> apply(
                    @io.reactivex.annotations.NonNull
                            List<UnsplashCollectionResponse> unsplashCollectionResponses
            ) throws Exception {
                List<Collection> collectionList = new ArrayList<Collection>();
                for (UnsplashCollectionResponse collectionResponse : unsplashCollectionResponses) {
                    collectionList.add(collectionResponse.toCollection());
                }
                return collectionList;
            }
        });
    }
}
