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
import com.aashreys.walls.domain.display.collections.FlickrRecentCollection;
import com.aashreys.walls.domain.display.collections.FlickrTag;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashRecentCollection;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/02/17.
 */

public class SourceFactory {

    private final UnsplashCollectionSourceFactory unsplashCollectionSourceFactory;

    private final UnsplashRecentSourceFactory unsplashRecentSourceFactory;

    private final FavoriteSourceFactory favoriteSourceFactory;

    private final FlickrRecentSourceFactory flickrRecentSourceFactory;

    private final FlickrTagSourceFactory flickrTagSourceFactory;

    @Inject
    public SourceFactory(
            UnsplashCollectionSourceFactory unsplashCollectionSourceFactory,
            UnsplashRecentSourceFactory unsplashRecentSourceFactory,
            FavoriteSourceFactory favoriteSourceFactory,
            FlickrRecentSourceFactory flickrRecentSourceFactory,
            FlickrTagSourceFactory flickrTagSourceFactory
    ) {
        this.unsplashCollectionSourceFactory = unsplashCollectionSourceFactory;
        this.unsplashRecentSourceFactory = unsplashRecentSourceFactory;
        this.favoriteSourceFactory = favoriteSourceFactory;
        this.flickrRecentSourceFactory = flickrRecentSourceFactory;
        this.flickrTagSourceFactory = flickrTagSourceFactory;
    }

    public Source create(Collection collection) {
        if (collection instanceof UnsplashRecentCollection) {
            return unsplashRecentSourceFactory.create();
        }
        if (collection instanceof UnsplashCollection) {
            return unsplashCollectionSourceFactory.create(collection.getId());
        }
        if (collection instanceof DiscoverCollection) {
            return new DiscoverSource(
                    flickrRecentSourceFactory.create(),
                    unsplashRecentSourceFactory.create()
            );
        }
        if (collection instanceof FavoriteCollection) {
            return favoriteSourceFactory.create();
        }
        if (collection instanceof FlickrTag) {
            return flickrTagSourceFactory.create(collection.getName());
        }
        if (collection instanceof FlickrRecentCollection) {
            return flickrRecentSourceFactory.create();
        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
