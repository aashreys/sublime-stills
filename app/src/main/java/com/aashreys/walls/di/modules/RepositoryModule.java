package com.aashreys.walls.di.modules;

import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.persistence.collections.CollectionRepositoryImpl;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepositoryImpl;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class RepositoryModule {

    public RepositoryModule() {}

    @Provides
    public ShortUrlRepository providesShortUrlRepository() {
        return new ShortUrlRepositoryImpl();
    }

    @Provides
    public CollectionRepository providesCollectionRepository(CollectionFactory collectionFactory) {
        return new CollectionRepositoryImpl(collectionFactory);
    }

    @Provides
    public FavoriteImageRepository providesFavoriteImageRepository() {
        return new FavoriteImageRepositoryImpl();
    }

}
