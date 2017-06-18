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

import java.util.List;

import io.reactivex.Single;

/**
 * Created by aashreys on 05/03/17.
 */

public class CollectionDiscoveryServiceImpl implements CollectionDiscoveryService {

    private final UnsplashCollectionDiscoveryService unsplashCollectionSearchService;

    public CollectionDiscoveryServiceImpl(
            UnsplashCollectionDiscoveryService unsplashCollectionSearchService
    ) {
        this.unsplashCollectionSearchService = unsplashCollectionSearchService;
    }

    @NonNull
    @Override
    public Single<List<Collection>> search(String collection, int minSize) {
        return unsplashCollectionSearchService.search(collection, minSize);
    }

    @NonNull
    @Override
    public Single<List<Collection>> getFeatured() {
        return unsplashCollectionSearchService.getFeatured();
    }

}
