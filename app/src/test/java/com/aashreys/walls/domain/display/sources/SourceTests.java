package com.aashreys.walls.domain.display.sources;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.network.ApiFactory;
import com.aashreys.walls.network.unsplash.UnsplashApi;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aashreys on 27/05/17.
 */

public class SourceTests extends BaseTestCase {

    private static final String RESPONSE_FILE = "response_get_photos.json";

    private MockWebServer mockWebServer;

    private UnsplashApi unsplashApi;

    private UnsplashCollectionSource unsplashCollectionSource;

    private UnsplashRecentSource unsplashRecentSource;

    private DiscoverSource discoverSource;

    @Override
    public void init() {
        super.init();
        mockWebServer = new MockWebServer();
        unsplashApi = new ApiFactory().createUnsplashApi(
                new OkHttpClient(),
                mockWebServer.url("").toString()
        );
        unsplashCollectionSource = new UnsplashCollectionSource(unsplashApi, new Id("123"));
        unsplashRecentSource = new UnsplashRecentSource(unsplashApi);
        discoverSource = new DiscoverSource(unsplashRecentSource);
    }

    @Test
    public void test_get_images_from_unsplash_collection_source() throws IOException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile(RESPONSE_FILE)));
        List<Image> imageList = unsplashCollectionSource.getImages(0).blockingGet();
        assertEquals(imageList.size(), 4);

        UnsplashImage image1 = (UnsplashImage) imageList.get(0);
        assertEquals(image1.getId().value(), "UrAKbpKnak8");
        assertEquals(image1.getRawImageUrl().value(), "https://images.unsplash.com/photo-1495667496513-9068843d7679");
        assertEquals(image1.getShareUrl().value(), "http://unsplash.com/photos/UrAKbpKnak8");
    }

    @Test
    public void test_get_images_from_unsplash_recent_source() throws IOException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile(RESPONSE_FILE)));
        List<Image> imageList = unsplashRecentSource.getImages(0).blockingGet();
        assertEquals(imageList.size(), 4);

        UnsplashImage image1 = (UnsplashImage) imageList.get(0);
        assertEquals(image1.getId().value(), "UrAKbpKnak8");
        assertEquals(image1.getRawImageUrl().value(), "https://images.unsplash.com/photo-1495667496513-9068843d7679");
        assertEquals(image1.getShareUrl().value(), "http://unsplash.com/photos/UrAKbpKnak8");
    }

    @Test
    public void test_get_images_from_discover_source() throws IOException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile(RESPONSE_FILE)));
        List<Image> imageList = discoverSource.getImages(0).blockingGet();
        assertEquals(imageList.size(), 4);

        UnsplashImage image1 = (UnsplashImage) imageList.get(0);
        assertEquals(image1.getId().value(), "UrAKbpKnak8");
        assertEquals(image1.getRawImageUrl().value(), "https://images.unsplash.com/photo-1495667496513-9068843d7679");
        assertEquals(image1.getShareUrl().value(), "http://unsplash.com/photos/UrAKbpKnak8");
    }

}
