package com.aashreys.walls.domain.display.sources;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
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
            return unsplashCollectionSourceFactory.create(collection.id());
        }
        if (collection instanceof UnsplashRecentCollection) {
            return unsplashRecentSourceFactory.create();
        }
        if (collection instanceof FavoriteCollection) {
            return favoriteSourceFactory.create();
        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
