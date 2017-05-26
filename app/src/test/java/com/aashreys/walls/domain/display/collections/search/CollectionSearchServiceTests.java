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

package com.aashreys.walls.domain.display.collections.search;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.Collection.Type;
import com.aashreys.walls.domain.display.collections.CollectionTests;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.network.apis.ApiFactory;
import com.aashreys.walls.network.apis.UnsplashApi;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class CollectionSearchServiceTests extends BaseTestCase {

    private MockWebServer mockWebServer;

    private UnsplashApi unsplashApi;

    private CollectionSearchServiceImpl collectionSearcher;

    @Override
    public void init() {
        super.init();
        mockWebServer = new MockWebServer();
        unsplashApi = new ApiFactory().createUnsplashApi(
                new OkHttpClient(),
                mockWebServer.url("").toString()
        );
        collectionSearcher = new CollectionSearchServiceImpl(new UnsplashCollectionSearchService(unsplashApi));
    }

    @Test
    public void test_collection_search() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile("response_search_collections.json")));
        List<Collection> collectionList = collectionSearcher.search("", 10);
        assertEquals(collectionList.size(), 4);

        Collection collection1 = collectionList.get(0);
        CollectionTests.testCollection(
                collection1,
                "497",
                "fire, sun & lights",
                Type.UNSPLASH_COLLECTION,
                true,
                UnsplashCollection.class
        );
    }

    @Test
    public void test_collection_search_min_size() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile("response_search_collections.json")));
        List<Collection> collectionList = collectionSearcher.search("", 50);
        // Only 3 collections are larger than 50 photos.
        assertEquals(collectionList.size(), 3);

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile("response_search_collections.json")));
        collectionList = collectionSearcher.search("", 150);
        // Only 3 collections are larger than 50 photos.
        assertEquals(collectionList.size(), 0);
    }

    @Test
    public void test_featured_collection() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile("response_get_featured_collections.json")));
        List<Collection> collectionList = collectionSearcher.getFeatured();
        assertEquals(collectionList.size(), 4);
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

}
