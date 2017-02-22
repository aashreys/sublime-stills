package com.aashreys.walls.di.modules;

import com.aashreys.walls.domain.display.collections.CollectionValidator;
import com.aashreys.walls.domain.display.collections.CollectionValidatorImpl;
import com.aashreys.walls.domain.display.collections.UnsplashCollectionSearcher;
import com.aashreys.walls.domain.display.images.ImageValidator;
import com.aashreys.walls.domain.display.sources.ImageValidatorImpl;
import com.aashreys.walls.domain.display.sources.UnsplashImageResponseParser;
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
    public ImageValidator providesImageValidator() {
        return new ImageValidatorImpl();
    }

    @Provides
    public CollectionValidator providesCollectionValidator() {
        return new CollectionValidatorImpl();
    }

    @Provides
    public UnsplashImageResponseParser providesUnsplashImageResponseParser(
            ImageValidator
                    imageValidator
    ) {
        return new UnsplashImageResponseParser(imageValidator);
    }

    @Provides
    public CollectionSearchTaskFactory providesCollectionSearchTaskFactory
            (Provider<UnsplashCollectionSearcher> unsplashImageResponseParserProvider) {
        return new CollectionSearchTaskFactory(unsplashImageResponseParserProvider);
    }

}
