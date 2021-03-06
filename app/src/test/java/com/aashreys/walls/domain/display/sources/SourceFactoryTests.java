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

import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.unsplash.UnsplashApi;
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
        Provider<UnsplashApi> mockUnsplashApiProvider = new Provider<UnsplashApi>() {
            @Override
            public UnsplashApi get() {
                return null;
            }
        };
        Provider<FavoriteImageRepository> mockFavoriteImageRepositoryProvider = new Provider
                <FavoriteImageRepository>() {
            @Override
            public FavoriteImageRepository get() {
                return null;
            }
        };

        SourceFactory sourceFactory = new SourceFactory(
                new UnsplashCollectionSourceFactory(mockUnsplashApiProvider),
                new UnsplashRecentSourceFactory(mockUnsplashApiProvider),
                new FavoriteSourceFactory(mockFavoriteImageRepositoryProvider)
        );

        Id id = new Id("2314");
        Name name = new Name("Office");
        Source unsplashCollectionSource = sourceFactory.create(new UnsplashCollection(
                id,
                name
        ));
        Source discoverSource = sourceFactory.create(new DiscoverCollection());
        Source favoriteSource = sourceFactory.create(new FavoriteCollection());

        assertTrue(unsplashCollectionSource instanceof UnsplashCollectionSource);
        assertTrue(discoverSource instanceof DiscoverSource);
        assertTrue(favoriteSource instanceof FavoriteSource);
    }
}
