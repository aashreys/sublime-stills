package com.aashreys.walls.sources;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.Config;
import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.ImageValidatorImpl;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSource;
import com.aashreys.walls.domain.display.sources.UnsplashImageResponseParser;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSource;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.network.UnsplashNetworkService;
import com.aashreys.walls.samples.UnsplashSourceSamples;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by aashreys on 15/02/17.
 */

public class UnsplashSourceTests extends MockitoTestCase {

    private static final String COLLECTION_ID = "23";

    // Search for 30 results on page 1
    private static final String UNSPLASH_RECENT_COLLECTION_URL
            = "https://api.unsplash.com/photos?page=1&per_page=30";

    // Search collection office for 30 results on page 1
    private static final String UNSPLASH_COLLECTION_URL =
            "https://api.unsplash.com//collections/" + COLLECTION_ID + "/photos?page=1&per_page=30";

    @Mock private UnsplashNetworkService networkService;

    private UnsplashRecentSource recentSource;

    private UnsplashCollectionSource collectionSource;

    @Override
    public void init() {
        super.init();
        collectionSource = new UnsplashCollectionSource(
                networkService,
                new UnsplashImageResponseParser(new ImageValidatorImpl()),
                new ServerId(COLLECTION_ID)
        );
        recentSource = new UnsplashRecentSource(
                networkService,
                new UnsplashImageResponseParser(new ImageValidatorImpl())
        );
    }

    @Test
    public void test_with_null_network_response() {
        when(networkService.get(
                UNSPLASH_RECENT_COLLECTION_URL,
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(null);
        List<Image> recentImageList = recentSource.getImages(0);
        assertEquals(recentImageList.size(), 0);

        when(networkService.get(
                UNSPLASH_COLLECTION_URL,
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(null);
        List<Image> collectionImageList = collectionSource.getImages(0);
        assertEquals(collectionImageList.size(), 0);
    }

    @Test
    public void test_with_valid_network_response() {
        when(networkService.get(
                UNSPLASH_RECENT_COLLECTION_URL,
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(UnsplashSourceSamples.VALID_RESPONSE);
        List<Image> recentImageList = recentSource.getImages(0);
        assertEquals(recentImageList.size(), 1);
        Image recentImage = recentImageList.get(0);
        assertEquals(recentImage.serverId().value(), "7j3SK5tI7Pw");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 16, 10, 54, 11);
        // Date accurate to 100ms since we don't get exactly the same date from our test object
        assertEquals(recentImage.createdAt().getTime() - calendar.getTime().getTime() < 100, true);
        assertEquals(recentImage.resX().value(), Integer.valueOf(3632));
        assertEquals(recentImage.resY().value(), Integer.valueOf(5113));
        assertEquals(recentImage.userRealName().value(), "Keap");
        assertEquals(recentImage.userProfileUrl().value(), "http://unsplash.com/@keap");
        assertEquals(recentImage.userPortfolioUrl().value(), "https://www.keapbk.com/");
        assertEquals(
                recentImage.smallImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s" +
                        "=de78ad845ef3ac9705864c1c336cdd4e"
        );
        assertEquals(
                recentImage.regularImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s" +
                        "=766bfd9c91667371df15460ecaedd3a9"
        );
        assertEquals(
                recentImage.fullImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&s=13cb6816b03a6c1b0deae7ffb8691cf3"
        );
        assertEquals(recentImage.serviceName().value(), "Unsplash");
        assertEquals(recentImage.serviceUrl().value(), "https://unsplash.com");

        when(networkService.get(
                UNSPLASH_COLLECTION_URL,
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(UnsplashSourceSamples.VALID_RESPONSE);

        List<Image> collectionImageList = collectionSource.getImages(0);
        assertEquals(collectionImageList.size(), 1);
        Image collectionImage = collectionImageList.get(0);
        assertEquals(collectionImage.serverId().value(), "7j3SK5tI7Pw");
        // Date accurate to 100ms since we don't get exactly the same date from our test object
        assertEquals(
                collectionImage.createdAt().getTime() - calendar.getTime().getTime() < 100,
                true
        );
        assertEquals(collectionImage.resX().value(), Integer.valueOf(3632));
        assertEquals(collectionImage.resY().value(), Integer.valueOf(5113));
        assertEquals(collectionImage.userRealName().value(), "Keap");
        assertEquals(collectionImage.userProfileUrl().value(), "http://unsplash.com/@keap");
        assertEquals(collectionImage.userPortfolioUrl().value(), "https://www.keapbk.com/");
        assertEquals(
                collectionImage.smallImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s" +
                        "=de78ad845ef3ac9705864c1c336cdd4e"
        );
        assertEquals(
                collectionImage.regularImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s" +
                        "=766bfd9c91667371df15460ecaedd3a9"
        );
        assertEquals(
                collectionImage.fullImageUrl().value(),
                "https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3" +
                        ".5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&s=13cb6816b03a6c1b0deae7ffb8691cf3"
        );
        assertEquals(collectionImage.serviceName().value(), "Unsplash");
        assertEquals(collectionImage.serviceUrl().value(), "https://unsplash.com");
    }

}
