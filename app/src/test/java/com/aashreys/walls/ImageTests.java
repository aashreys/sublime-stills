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

package com.aashreys.walls;

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Coordinates;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.Service;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class ImageTests extends BaseTestCase {

    private UnsplashImage _createUnsplashImage(
            String id,
            int x,
            int y,
            long createdAtTime,
            String userId,
            String userRealName,
            String userProfileUrl,
            String userPortfolioUrl,
            String rawImageUrl,
            String imageShareUrl
    ) {
        try {
            return new UnsplashImage(
                    new Id(id),
                    new Url(rawImageUrl),
                    new Url(imageShareUrl),
                    null,
                    new Date(createdAtTime),
                    new User(
                            new Id(userId),
                            new Name(userRealName),
                            new Url(userProfileUrl),
                            new Url(userPortfolioUrl)
                    ),
                    null,
                    null,
                    new Resolution(new Pixel(x), new Pixel(y)),
                    null
            );
        } catch (InstantiationException e) {
            return null;
        }
    }

    private Exif createExif(
            String camera,
            String exposureTime,
            String aperture,
            String focalLength,
            String iso
    ) {
        try {
            return new Exif(
                    new Name(camera),
                    new Name(exposureTime),
                    new Name(aperture),
                    new Name(focalLength),
                    new Name(iso)
            );
        } catch (InstantiationException e) {
            return null;
        }
    }

    private Location createLocation(String locationName, double latitude, double longitude) {
        try {
            return new Location(new Name(locationName), new Coordinates(latitude, longitude));
        } catch (InstantiationException e) {
            return null;
        }
    }

    @Test
    public void test_image_constructor_args() {
        // Constructing image with valid arguments
        int deviceWidth = 1080;
        String id = "116";
        String title = "The Composer";
        String rawImageUrl = "http://composer.com/theComposed.jpg";
        String imageShareUrl = "http://imageShareUrl.com";
        Integer x = 35;
        Integer y = 55;
        long createdAtTime = 253543;
        String userId = "231sfDic";
        String userRealName = "The Diadact";
        String userProfileUrl = "http://composeAllTheThings.com";
        String userPortfolioUrl = "http://composerLibrary.com";

        String locationNameString = "The Hilltop";
        double latitude = 24.12;
        double longitude = 23.42;
        Location location = createLocation(locationNameString, latitude, longitude);

        String camera = "Nikon D500";
        String exposureTime = "400 ms";
        String aperture = "f/4.0";
        String focalLength = "24 mm";
        String iso = "1000";
        Exif exif = createExif(camera, exposureTime, aperture, focalLength, iso);

        UnsplashImage unsplashImage = _createUnsplashImage(
                id,
                x,
                y,
                createdAtTime,
                userId,
                userRealName,
                userProfileUrl,
                userPortfolioUrl,
                rawImageUrl,
                imageShareUrl
        );
        unsplashImage.setLocation(location);
        unsplashImage.setExif(exif);

        assertEquals(unsplashImage.getId().value(), id);
        assertEquals(
                unsplashImage.getUrl(deviceWidth).value(),
                rawImageUrl.concat(String.format(UnsplashImage.IMAGE_URL_CONFIG, deviceWidth))
        );
        assertEquals(unsplashImage.getShareUrl().value(), imageShareUrl);
        assertEquals(unsplashImage.getTitle(), null);

        unsplashImage.setTitle(new Name(title));
        assertEquals(unsplashImage.getTitle().value(), title);

        assertEquals(unsplashImage.getResolution().getWidth().value(), x);
        assertEquals(unsplashImage.getResolution().getHeight().value(), y);

        assertEquals(unsplashImage.getUploadDate().getTime(), createdAtTime);

        User unsplashUser = unsplashImage.getUser();
        assertEquals(unsplashUser.getId().value(), userId);
        assertEquals(unsplashUser.getName().value(), userRealName);
        assertEquals(unsplashUser.getProfileUrl().value(), userProfileUrl);
        assertEquals(unsplashUser.getPortfolioUrl().value(), userPortfolioUrl);

        Service unsplashService = unsplashImage.getService();
        assertEquals(unsplashService.getName().value(), "Unsplash");
        assertEquals(unsplashService.getUrl().value(), "https://unsplash.com");

        location = unsplashImage.getLocation();
        assertEquals(location.getName().value(), locationNameString);
        assertEquals(location.getCoordinates().getLongitude(), longitude, 0);
        assertEquals(location.getCoordinates().getLatitude(), latitude, 0);

        exif = unsplashImage.getExif();
        assertEquals(exif.camera.value(), camera);
        assertEquals(exif.exposureTime.value(), exposureTime);
        assertEquals(exif.aperture.value(), aperture);
        assertEquals(exif.focalLength.value(), focalLength);
        assertEquals(exif.iso.value(), iso);
    }

}
