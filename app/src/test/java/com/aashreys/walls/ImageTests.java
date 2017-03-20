package com.aashreys.walls;

import com.aashreys.walls.domain.display.images.FlickrImage;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Service;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.display.images.utils.FlickrBaseEncoder;
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

    public static UnsplashImage _createUnsplashImage(
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
        return new UnsplashImage(
                new Id(id),
                new Pixel(x),
                new Pixel(y),
                new Date(createdAtTime),
                new Id(userId),
                new Name(userRealName),
                new Url(userProfileUrl),
                new Url(userPortfolioUrl),
                new Url(rawImageUrl),
                new Url(imageShareUrl)
        );
    }

    public static FlickrImage _createFlickImage(
            String id,
            String ownerId,
            String serverId,
            String farmId,
            String secret,
            String title,
            String userName,
            Date createdAt,
            Location location
            ) {
        return new FlickrImage(
                new Id(id),
                new Id(ownerId),
                new Id(serverId),
                new Id(farmId),
                new Id(secret),
                new Name(title),
                new Name(userName),
                createdAt,
                location
        );
    }

    @Test
    public void test_image_constructor_args() {
        // Constructing image with valid arguments
        int deviceWidth = 1080;
        String id = "116"; // Not John-117
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
        String serviceName = "The Composer";
        String serviceUrl = "http://composer.com";
        String locationNameString = "The Hilltop";
        double latitude = 24.12;
        double longitude = 23.42;
        Location location = new Location(new Name(locationNameString), longitude, latitude);

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
        assertEquals(unsplashImage.getId().value(), id);
        assertEquals(unsplashImage.getUrl(deviceWidth).value(),
                rawImageUrl.concat(String.format(Unsplash.IMAGE_URL_CONFIG, deviceWidth))
        );
        assertEquals(unsplashImage.getShareUrl().value(), imageShareUrl);
        assertEquals(unsplashImage.getTitle(), null);

        unsplashImage.setTitle(new Name(title));
        assertEquals(unsplashImage.getTitle().value(), title);

        assertEquals(unsplashImage.getResolution().getResX().value(), x);
        assertEquals(unsplashImage.getResolution().getResY().value(), y);

        assertEquals(unsplashImage.getUploadDate().getTime(), createdAtTime);

        User unsplashUser = unsplashImage.getUser();
        assertEquals(unsplashUser.getId().value(), userId);
        assertEquals(unsplashUser.getName().value(), userRealName);
        assertEquals(unsplashUser.getProfileUrl().value(), userProfileUrl);
        assertEquals(unsplashUser.getPortfolioUrl().value(), userPortfolioUrl);

        Service unsplashService = unsplashImage.getService();
        assertEquals(unsplashService.getName().value(), "Unsplash");
        assertEquals(unsplashService.getUrl().value(), "https://unsplash.com");

        // Flickr specific data
        String serverId = "3123";
        String farmId = "4";
        String secret = "31243535";
        // Urls constructed from Flickr's photo url template using the data above.
        String imageUrl = "https://farm4.staticflickr.com/3123/116_31243535_b.jpg";
        imageShareUrl = "https://flic.kr/p/" + FlickrBaseEncoder.encode(Long.valueOf(id)) + "/";
        Image flickrImage = _createFlickImage(
                id,
                userId,
                serverId,
                farmId,
                secret,
                title,
                userRealName,
                new Date(createdAtTime),
                location
        );
        assertEquals(flickrImage.getId().value(), id);
        assertEquals(flickrImage.getUrl(deviceWidth).value(), imageUrl);
        assertEquals(flickrImage.getShareUrl().value(), imageShareUrl);
    }



    private static class Unsplash {

        private static final String IMAGE_URL_CONFIG = "?q=80&fm=jpg&w=600&fit=max";

    }

}
