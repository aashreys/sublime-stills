package com.aashreys.walls.sources;

import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashRecentCollection;
import com.aashreys.walls.domain.display.sources.FavoriteSource;
import com.aashreys.walls.domain.display.sources.FavoriteSourceFactory;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSource;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashImageResponseParser;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSource;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSourceFactory;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.network.UnsplashNetworkService;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;

import org.junit.Test;

import javax.inject.Provider;

import static org.junit.Assert.assertEquals;

/**
 * Created by aashreys on 18/02/17.
 */

public class SourceFactoryTests extends MockitoTestCase {

    @Test
    public void test_return_types() {
        Provider<UnsplashNetworkService> networkServiceProvider = new Provider
                <UnsplashNetworkService>() {
            @Override
            public UnsplashNetworkService get() {
                return null;
            }
        };
        Provider<UnsplashImageResponseParser> unsplashImageResponseParserProvider = new Provider
                <UnsplashImageResponseParser>() {
            @Override
            public UnsplashImageResponseParser get() {
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
        SourceFactory sourceFactory = new SourceFactory(
                new UnsplashCollectionSourceFactory(networkServiceProvider, unsplashImageResponseParserProvider),
                new UnsplashRecentSourceFactory(networkServiceProvider, unsplashImageResponseParserProvider),
                new FavoriteSourceFactory(favoriteImageRepositoryProvider)
        );

        Source unsplashRecentSource = sourceFactory.create(new UnsplashRecentCollection());
        ServerId serverId = new ServerId("2314");
        Name name = new Name("Office");
        Source unsplashCollectionSource = sourceFactory.create(new UnsplashCollection(serverId, name));
        Source favoriteSource = sourceFactory.create(new FavoriteCollection());

        assertEquals(unsplashRecentSource instanceof UnsplashRecentSource, true);
        assertEquals(unsplashCollectionSource instanceof UnsplashCollectionSource, true);
        assertEquals(favoriteSource instanceof FavoriteSource, true);
    }


}
