package com.aashreys.walls.di.modules;

import com.aashreys.walls.domain.display.collections.search.CollectionSearchService;
import com.aashreys.walls.domain.display.collections.search.CollectionSearchServiceImpl;
import com.aashreys.walls.domain.display.collections.search.FlickrTagSearchService;
import com.aashreys.walls.domain.display.collections.search.UnsplashCollectionSearchService;
import com.aashreys.walls.domain.display.images.ImagePropertiesService;
import com.aashreys.walls.domain.display.images.ImagePropertiesServiceImpl;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerImpl;
import com.aashreys.walls.network.apis.FlickrApi;
import com.aashreys.walls.network.apis.UrlShortenerApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoResponseParser;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;
import com.aashreys.walls.ui.tasks.CollectionSearchTaskFactory;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class UtilsModule {

    public UtilsModule() {}

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
    public UrlShortener providesUrlShortener(
            ShortUrlRepository shortUrlRepository,
            UrlShortenerApi urlShortenerApi
    ) {
        return new UrlShortenerImpl(shortUrlRepository, urlShortenerApi);
    }

    @Provides
    public ImagePropertiesService providesImagePropertiesService(FlickrApi flickrApi) {
        return new ImagePropertiesServiceImpl(flickrApi);
    }

    @Provides
    public CollectionSearchService providesCollectionSearchService(
            UnsplashCollectionSearchService unsplashCollectionSearchService,
            FlickrTagSearchService flickrTagSearchService
    ) {
        return new CollectionSearchServiceImpl(
                unsplashCollectionSearchService,
                flickrTagSearchService
        );
    }

}
