package com.aashreys.walls.di.modules;

import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.display.sources.FavoriteSourceFactory;
import com.aashreys.walls.domain.display.sources.FlickrRecentSourceFactory;
import com.aashreys.walls.domain.display.sources.FlickrTagSourceFactory;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSourceFactory;
import com.aashreys.walls.domain.share.ImageUrlSharerFactory;
import com.aashreys.walls.domain.share.SharerFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class FactoryModule {

    public FactoryModule() {}

    @Provides
    public SourceFactory providesSourceFactory(
            UnsplashCollectionSourceFactory unsplashCollectionSourceFactory,
            UnsplashRecentSourceFactory unsplashRecentSourceFactory,
            FavoriteSourceFactory favoriteSourceFactory,
            FlickrRecentSourceFactory flickrRecentSourceFactory,
            FlickrTagSourceFactory flickrTagSourceFactory
    ) {
        return new SourceFactory(
                unsplashCollectionSourceFactory,
                unsplashRecentSourceFactory,
                favoriteSourceFactory,
                flickrRecentSourceFactory,
                flickrTagSourceFactory
        );
    }

    @Provides
    public SharerFactory providesSharerFactory(
            ImageUrlSharerFactory imageUrlSharerFactory,
            DeviceResolution deviceResolution
    ) {
        return new SharerFactory(imageUrlSharerFactory, deviceResolution);
    }

    @Provides
    public CollectionFactory providesCollectionFactory() {
        return new CollectionFactory();
    }

}
