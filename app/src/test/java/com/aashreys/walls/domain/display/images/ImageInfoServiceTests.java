package com.aashreys.walls.domain.display.images;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.network.apis.ApiFactory;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoInfoParser;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aashreys on 26/05/17.
 */

public class ImageInfoServiceTests extends BaseTestCase {

    private MockWebServer mockWebServer;

    private UnsplashApi unsplashApi;

    private ImageInfoServiceImpl imageInfoService;

    @Override
    public void init() {
        super.init();
        mockWebServer = new MockWebServer();
        unsplashApi = new ApiFactory().createUnsplashApi(
                new OkHttpClient(),
                mockWebServer.url("").toString()
        );
        imageInfoService = new ImageInfoServiceImpl(unsplashApi, new UnsplashPhotoInfoParser());
    }

    @Test
    public void test_image_info() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(readFile("response_get_photo_info.json")));
        UnsplashImage image = ImageTests.createUnsplashImage("123", "http://google.com", "http://google.com");

        final CountDownLatch latch = new CountDownLatch(1);
        imageInfoService.addInfo(image, new ImageInfoService.Listener() {
            @Override
            public void onComplete(Image imageWithProperties) {
                testExif(
                        imageWithProperties.getExif(),
                        "Canon EOS 5D Mark III",
                        "1/4000 sec",
                        "f/4",
                        "105 mm",
                        "100"
                );
                testLocation(
                        imageWithProperties.getLocation(),
                        "Sydney, Australia",
                        -33.8688197,
                        151.2092955
                );
                latch.countDown();
            }
        });
        latch.await();
    }

    private void testExif(
            Exif exif,
            String expectedCamera,
            String expectedExposureTime,
            String expectedAperture,
            String expectedFocalLength,
            String expectedIso
    ) {
        System.out.print(exif.toString());
        assertEquals(exif.camera.value(), expectedCamera);
        assertEquals(exif.exposureTime.value(), expectedExposureTime);
        assertEquals(exif.aperture.value(), expectedAperture);
        assertEquals(exif.focalLength.value(), expectedFocalLength);
        assertEquals(exif.iso.value(), expectedIso);
    }

    private void testLocation(
            Location location,
            String expectedLocationName,
            double expectedLatitude,
            double expectedLongitude
    ) {
        System.out.print(location.toString());
        assertEquals(location.getName().value(), expectedLocationName);
        assertEquals(location.getCoordinates().getLatitude(), expectedLatitude);
        assertEquals(location.getCoordinates().getLongitude(), expectedLongitude);
    }

}
