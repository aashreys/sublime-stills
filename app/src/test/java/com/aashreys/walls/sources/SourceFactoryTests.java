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

package com.aashreys.walls.sources;

import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.FlickrRecentCollection;
import com.aashreys.walls.domain.display.collections.FlickrTag;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashRecentCollection;
import com.aashreys.walls.domain.display.sources.DiscoverSource;
import com.aashreys.walls.domain.display.sources.FavoriteSource;
import com.aashreys.walls.domain.display.sources.FavoriteSourceFactory;
import com.aashreys.walls.domain.display.sources.FlickrRecentSource;
import com.aashreys.walls.domain.display.sources.FlickrRecentSourceFactory;
import com.aashreys.walls.domain.display.sources.FlickrTagSource;
import com.aashreys.walls.domain.display.sources.FlickrTagSourceFactory;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSource;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSource;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSourceFactory;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.apis.FlickrApi;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.FlickrPhotoArrayParser;
import com.aashreys.walls.network.parsers.UnsplashPhotoResponseParser;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;

import org.junit.Test;

import javax.inject.Provider;

import static junit.framework.Assert.assertTrue;

/**
 * Created by aashreys on 18/02/17.
 */

public class SourceFactoryTests extends MockitoTestCase {

    @Test
    public void test_return_types() {
        Provider<UnsplashApi> unsplashApiProvider = new Provider<UnsplashApi>() {
            @Override
            public UnsplashApi get() {
                return null;
            }
        };
        Provider<UnsplashPhotoResponseParser> unsplashImageResponseParserProvider = new Provider
                <UnsplashPhotoResponseParser>() {
            @Override
            public UnsplashPhotoResponseParser get() {
                return null;
            }
        };
        Provider<FavoriteImageRepository> favoriteImageRepositoryProvider = new Provider
                <FavoriteImageRepository>() {
            @Override
            public FavoriteImageRepository get() {
                return null;
            }
        };
        Provider<FlickrApi> flickrApiProvider = new Provider<FlickrApi>() {
            @Override
            public FlickrApi get() {
                return null;
            }
        };
        Provider<FlickrPhotoArrayParser> flickrPhotoArrayParserProvider = new Provider
                <FlickrPhotoArrayParser>() {
            @Override
            public FlickrPhotoArrayParser get() {
                return null;
            }
        };

        SourceFactory sourceFactory = new SourceFactory(
                new UnsplashCollectionSourceFactory(
                        unsplashApiProvider,
                        unsplashImageResponseParserProvider
                ),
                new UnsplashRecentSourceFactory(
                        unsplashApiProvider,
                        unsplashImageResponseParserProvider
                ),
                new FavoriteSourceFactory(favoriteImageRepositoryProvider),
                new FlickrRecentSourceFactory(flickrApiProvider, flickrPhotoArrayParserProvider),
                new FlickrTagSourceFactory(flickrApiProvider, flickrPhotoArrayParserProvider)
        );

        Id id = new Id("2314");
        Name name = new Name("Office");
        Source unsplashRecentSource = sourceFactory.create(new UnsplashRecentCollection());
        Source unsplashCollectionSource = sourceFactory.create(new UnsplashCollection(
                id,
                name
        ));
        Source discoverSource = sourceFactory.create(new DiscoverCollection());
        Source favoriteSource = sourceFactory.create(new FavoriteCollection());
        Source flickRecentSource = sourceFactory.create(new FlickrRecentCollection());
        Source flickTagSource = sourceFactory.create(new FlickrTag(name));

        assertTrue(unsplashRecentSource instanceof UnsplashRecentSource);
        assertTrue(unsplashCollectionSource instanceof UnsplashCollectionSource);
        assertTrue(discoverSource instanceof DiscoverSource);
        assertTrue(favoriteSource instanceof FavoriteSource);
        assertTrue(flickRecentSource instanceof FlickrRecentSource);
        assertTrue(flickTagSource instanceof FlickrTagSource);
    }
}
