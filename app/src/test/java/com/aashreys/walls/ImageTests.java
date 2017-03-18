package com.aashreys.walls;

import com.aashreys.walls.domain.display.images.FavoriteImage;
import com.aashreys.walls.domain.display.images.FlickrImage;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.info.ImageInfo;
import com.aashreys.walls.domain.display.images.info.Location;
import com.aashreys.walls.domain.display.images.utils.FlickrBaseEncoder;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;

import java.util.Date;

import static com.aashreys.walls.domain.display.images.Image.UrlType;
import static org.junit.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class ImageTests extends BaseTestCase {

    public static Image _createUnsplashImage(
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

    public static FavoriteImage _createFavoriteImage(
            String id,
            String title,
            String imageStreamUrl,
            String imageDetailUrl,
            String imageSetAsUrl,
            String imageShareUrl,
            int x,
            int y,
            long createAtTime,
            String userId,
            String userRealName,
            String userProfileUrl,
            String userPortfolioUrl,
            String serviceName,
            String serviceUrl
    ) {
        return new FavoriteImage(
                new Id(id),
                new Name(title),
                new Url(imageStreamUrl),
                new Url(imageDetailUrl),
                new Url(imageSetAsUrl),
                new Url(imageShareUrl),
                new Pixel(x),
                new Pixel(y),
                new Date(createAtTime),
                new Id(userId),
                new Name(userRealName),
                new Url(userProfileUrl),
                new Url(userPortfolioUrl),
                new Name(serviceName),
                new Url(serviceUrl)
        );
    }

    @Test
    public void test_image_constructor_args() {
        // Constructing image with valid arguments
        String id = "116"; // Not John-117
        String title = "The Composer";
        String rawImageUrl = "http://composer.com/theComposed.jpg";
        String imageStreamUrl = "http://imageStreamUrl.com";
        String imageDetailUrl = "http://imageDetailUrl.com";
        String imageSetAsUrl = "http://imageSetAsUrl.com";
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

        Image unsplashImage = _createUnsplashImage(
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
        assertEquals(
                unsplashImage.getUrl(UrlType.IMAGE_STREAM).value(),
                rawImageUrl.concat(Unsplash.IMAGE_STREAM_URL_OPTIONS)
        );
        assertEquals(
                unsplashImage.getUrl(UrlType.IMAGE_DETAIL).value(),
                rawImageUrl.concat(Unsplash.IMAGE_DETAIL_URL_OPTIONS)
        );
        assertEquals(
                unsplashImage.getUrl(UrlType.SET_AS).value(),
                rawImageUrl.concat(Unsplash.SET_AS_URL_OPTIONS)
        );
        assertEquals(unsplashImage.getUrl(UrlType.SHARE).value(), imageShareUrl);


        ImageInfo unsplashImageInfo = unsplashImage.getInfo();
        assertEquals(unsplashImageInfo.title, null);
        assertEquals(unsplashImageInfo.resX.value(), x);
        assertEquals(unsplashImageInfo.resY.value(), y);
        assertEquals(unsplashImageInfo.createdAt.getTime(), createdAtTime);
        assertEquals(unsplashImageInfo.userId.value(), userId);
        assertEquals(unsplashImageInfo.userRealName.value(), userRealName);
        assertEquals(unsplashImageInfo.userProfileUrl.value(), userProfileUrl);
        assertEquals(unsplashImageInfo.userPortfolioUrl.value(), userPortfolioUrl);
        assertEquals(unsplashImageInfo.serviceName.value(), "Unsplash");
        assertEquals(unsplashImageInfo.serviceUrl.value(), "https://unsplash.com");

        Image favoriteImage = _createFavoriteImage(
                id,
                title,
                imageStreamUrl,
                imageDetailUrl,
                imageSetAsUrl,
                imageShareUrl,
                x,
                y,
                createdAtTime,
                userId,
                userRealName,
                userProfileUrl,
                userPortfolioUrl,
                serviceName,
                serviceUrl
        );

        assertEquals(favoriteImage.getId().value(), id);
        assertEquals(favoriteImage.getUrl(UrlType.IMAGE_STREAM).value(), imageStreamUrl);
        assertEquals(favoriteImage.getUrl(UrlType.IMAGE_DETAIL).value(), imageDetailUrl);
        assertEquals(favoriteImage.getUrl(UrlType.SET_AS).value(), imageSetAsUrl);
        assertEquals(favoriteImage.getUrl(UrlType.SHARE).value(), imageShareUrl);

        ImageInfo favoriteImageInfo = favoriteImage.getInfo();
        assertEquals(favoriteImageInfo.title.value(), title);
        assertEquals(favoriteImageInfo.resX.value(), x);
        assertEquals(favoriteImageInfo.resY.value(), y);
        assertEquals(favoriteImageInfo.createdAt.getTime(), createdAtTime);
        assertEquals(favoriteImageInfo.userId.value(), userId);
        assertEquals(favoriteImageInfo.userRealName.value(), userRealName);
        assertEquals(favoriteImageInfo.userProfileUrl.value(), userProfileUrl);
        assertEquals(favoriteImageInfo.userPortfolioUrl.value(), userPortfolioUrl);
        assertEquals(favoriteImageInfo.serviceName.value(), serviceName);
        assertEquals(favoriteImageInfo.serviceUrl.value(), serviceUrl);


        // Flickr specific data
        String serverId = "3123";
        String farmId = "4";
        String secret = "31243535";
        // Urls constructed from Flickr's photo url template using the data above.
        imageStreamUrl = "https://farm4.staticflickr.com/3123/116_31243535_c.jpg";
        imageDetailUrl = "https://farm4.staticflickr.com/3123/116_31243535_h.jpg";
        imageSetAsUrl = "https://farm4.staticflickr.com/3123/116_31243535_h.jpg";
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
        assertEquals(
                flickrImage.getUrl(UrlType.IMAGE_STREAM).value(),
                imageStreamUrl
        );
        assertEquals(
                flickrImage.getUrl(UrlType.IMAGE_DETAIL).value(),
                imageDetailUrl
        );
        assertEquals(
                flickrImage.getUrl(UrlType.SET_AS).value(),
                imageSetAsUrl
        );
        assertEquals(flickrImage.getUrl(UrlType.SHARE).value(), imageShareUrl);
        ImageInfo flickrImageInfo = flickrImage.getInfo();
        assertEquals(flickrImageInfo.userId.value(), userId);
        assertEquals(flickrImageInfo.serviceName.value(), "Flickr");
        assertEquals(flickrImageInfo.serviceUrl.value(), "https://flickr.com");
        assertEquals(flickrImageInfo.title.value(), title);
        assertEquals(flickrImageInfo.createdAt.getTime(), createdAtTime);
        assertEquals(flickrImageInfo.location, location);
    }

    private static class Unsplash {

        private static final String IMAGE_STREAM_URL_OPTIONS = "?q=80&fm=jpg&w=600&fit=max";

        private static final String IMAGE_DETAIL_URL_OPTIONS =
                "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

        private static final String SET_AS_URL_OPTIONS = "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

    }

}
