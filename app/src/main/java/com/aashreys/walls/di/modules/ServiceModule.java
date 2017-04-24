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

package com.aashreys.walls.di.modules;

import com.aashreys.walls.domain.display.collections.search.CollectionSearchService;
import com.aashreys.walls.domain.display.collections.search.CollectionSearchServiceImpl;
import com.aashreys.walls.domain.display.collections.search.UnsplashCollectionSearchService;
import com.aashreys.walls.domain.display.images.ImageInfoService;
import com.aashreys.walls.domain.display.images.ImageInfoServiceImpl;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerImpl;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.apis.UrlShortenerApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoInfoParser;
import com.aashreys.walls.network.parsers.UnsplashPhotoResponseParser;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;
import com.aashreys.walls.application.tasks.CollectionSearchTaskFactory;
import com.aashreys.walls.application.tasks.FeaturedCollectionsTaskFactory;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class ServiceModule {

    public ServiceModule() {}

    @Provides
    public UnsplashPhotoResponseParser providesUnsplashImageResponseParser() {
        return new UnsplashPhotoResponseParser();
    }

    @Provides
    public CollectionSearchTaskFactory providesCollectionSearchTaskFactory
            (Provider<CollectionSearchService> collectionSearchServiceProvider) {
        return new CollectionSearchTaskFactory(collectionSearchServiceProvider);
    }

    @Provides
    public FeaturedCollectionsTaskFactory providesFeaturedCollectionsTaskFactory
            (Provider<CollectionSearchService> collectionSearchServiceProvider) {
        return new FeaturedCollectionsTaskFactory(collectionSearchServiceProvider);
    }

    @Provides
    public UrlShortener providesUrlShortener(
            ShortUrlRepository shortUrlRepository,
            UrlShortenerApi urlShortenerApi
    ) {
        return new UrlShortenerImpl(shortUrlRepository, urlShortenerApi);
    }

    @Provides
    public ImageInfoService providesImagePropertiesService(
            UnsplashApi unsplashApi,
            UnsplashPhotoInfoParser unsplashPhotoInfoParser
    ) {
        return new ImageInfoServiceImpl(
                unsplashApi,
                unsplashPhotoInfoParser
        );
    }

    @Provides
    public CollectionSearchService providesCollectionSearchService(
            UnsplashCollectionSearchService unsplashCollectionSearchService
    ) {
        return new CollectionSearchServiceImpl(unsplashCollectionSearchService);
    }

}
