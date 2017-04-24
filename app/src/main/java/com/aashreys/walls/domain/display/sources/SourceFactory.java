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

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/02/17.
 */

public class SourceFactory {

    private final UnsplashCollectionSourceFactory unsplashCollectionSourceFactory;

    private final UnsplashRecentSourceFactory unsplashRecentSourceFactory;

    private final FavoriteSourceFactory favoriteSourceFactory;

    @Inject
    public SourceFactory(
            UnsplashCollectionSourceFactory unsplashCollectionSourceFactory,
            UnsplashRecentSourceFactory unsplashRecentSourceFactory,
            FavoriteSourceFactory favoriteSourceFactory
    ) {
        this.unsplashCollectionSourceFactory = unsplashCollectionSourceFactory;
        this.unsplashRecentSourceFactory = unsplashRecentSourceFactory;
        this.favoriteSourceFactory = favoriteSourceFactory;
    }

    public Source create(Collection collection) {
        if (collection instanceof UnsplashCollection) {
            return unsplashCollectionSourceFactory.create(collection.getId());
        }
        if (collection instanceof DiscoverCollection) {
            return new DiscoverSource(unsplashRecentSourceFactory.create());
        }
        if (collection instanceof FavoriteCollection) {
            return favoriteSourceFactory.create();
        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
