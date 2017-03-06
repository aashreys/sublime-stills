package com.aashreys.walls.collections;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.Collection.Type;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.search.UnsplashCollectionSearchService;
import com.aashreys.walls.network.apis.ApiInstanceCreator;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.responses.CollectionSearcherResponse;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class CollectionSearcherTests extends BaseTestCase {

    private MockWebServer mockWebServer;

    private UnsplashApi unsplashApi;

    private UnsplashCollectionSearchService collectionSearcher;

    @Override
    public void init() {
        super.init();
        mockWebServer = new MockWebServer();
        HttpUrl baseUrl = mockWebServer.url("");
        unsplashApi = new ApiInstanceCreator().createUnsplashApi(
                new OkHttpClient(),
                baseUrl.toString()
        );
        collectionSearcher = new UnsplashCollectionSearchService(unsplashApi);
    }

    @Test
    public void test_search_with_invalid_response() throws IOException {
        String searchString = "hallelujah";
        mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(""));
        unsplashApi.searchCollections(searchString, 1, 10);
        List<Collection> collectionList = collectionSearcher.search(searchString);
        assertEquals(collectionList.size(), 0);
    }

    @Test
    public void test_search_with_valid_response() {
        String searchString = "hallelujah";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(CollectionSearcherResponse.VALID_RESPONSE));
        List<Collection> collectionList = collectionSearcher.search(searchString);
        assertEquals(collectionList.size(), 1);

        Collection collection1 = collectionList.get(0);
        CollectionTests.testCollection(
                collection1,
                "193913",
                "Office",
                Type.UNSPLASH_COLLECTION,
                true,
                UnsplashCollection.class
        );
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

}
