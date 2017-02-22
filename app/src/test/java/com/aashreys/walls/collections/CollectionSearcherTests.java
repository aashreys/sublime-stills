package com.aashreys.walls.collections;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.Config;
import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionValidatorImpl;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollectionSearcher;
import com.aashreys.walls.network.UnsplashNetworkService;
import com.aashreys.walls.samples.CollectionSearcherSamples;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by aashreys on 10/02/17.
 */

public class CollectionSearcherTests extends MockitoTestCase {

    private static final String URL = "https://api.unsplash.com/search/collections?query=%s";

    private static final String INVALID_RESPONSE =
            "bugs bunny doesn't like the internet, so no JSON for you.";

    @Mock private UnsplashNetworkService unsplashNetworkService;

    private UnsplashCollectionSearcher collectionSearcher;

    @Override
    public void init() {
        super.init();
        collectionSearcher = new UnsplashCollectionSearcher(
                new CollectionValidatorImpl(),
                unsplashNetworkService
        );
    }

    @Test
    public void test_null_input_search() {
        List<Collection> collectionList = collectionSearcher.search(null);
        assertEquals(collectionList.size(), 0);
    }

    @Test
    public void test_search_with_invalid_response() {
        String searchString = "hallelujah";
        when(unsplashNetworkService.get(
                String.format(URL, searchString),
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(INVALID_RESPONSE);
        List<Collection> collectionList = collectionSearcher.search("hallelujah");
        assertEquals(collectionList.size(), 0);
    }

    @Test
    public void test_search_with_valid_response() {
        String searchString = "hallelujah";
        when(unsplashNetworkService.get(
                String.format(URL, searchString),
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        )).thenReturn(CollectionSearcherSamples.VALID_RESPONSE);

        List<Collection> collectionList = collectionSearcher.search("hallelujah");
        assertEquals(collectionList.size(), 1);

        Collection collection1 = collectionList.get(0);
        CollectionTests.testCommonCollection(
                collection1,
                "Office",
                "193913",
                true,
                UnsplashCollection.class
        );
    }

}
