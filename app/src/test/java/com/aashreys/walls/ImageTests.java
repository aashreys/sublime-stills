package com.aashreys.walls;

import com.aashreys.walls.domain.display.images.FavoriteImage;
import com.aashreys.walls.domain.display.images.FlickrImage;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.utils.FlickrBaseEncoder;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;

import java.util.Date;

import static com.aashreys.walls.domain.display.images.Image.Properties;
import static com.aashreys.walls.domain.display.images.Image.UrlType;
import static org.junit.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class ImageTests extends BaseTestCase {

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
        long createAtTime = 253543;
        String userRealName = "The Diadact";
        String userProfileUrl = "http://composeAllTheThings.com";
        String userPortfolioUrl = "http://composerLibrary.com";
        String serviceName = "The Composer";
        String serviceUrl = "http://composer.com";

        Image unsplashImage = _createUnsplashImage(
                id,
                x,
                y,
                createAtTime,
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

        Properties unsplashImageProperties = unsplashImage.getProperties();
        assertEquals(unsplashImageProperties.title, null);
        assertEquals(unsplashImageProperties.resX.value(), x);
        assertEquals(unsplashImageProperties.resY.value(), y);
        assertEquals(unsplashImageProperties.createdAt.getTime(), createAtTime);
        assertEquals(unsplashImageProperties.userRealName.value(), userRealName);
        assertEquals(unsplashImageProperties.userProfileUrl.value(), userProfileUrl);
        assertEquals(unsplashImageProperties.userPortfolioUrl.value(), userPortfolioUrl);
        assertEquals(unsplashImageProperties.serviceName.value(), "Unsplash");
        assertEquals(unsplashImageProperties.serviceUrl.value(), "https://unsplash.com");

        Image favoriteImage = _createFavoriteImage(
                id,
                title,
                imageStreamUrl,
                imageDetailUrl,
                imageSetAsUrl,
                imageShareUrl,
                x,
                y,
                createAtTime,
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

        Properties favoriteImageProperties = favoriteImage.getProperties();
        assertEquals(favoriteImageProperties.title.value(), title);
        assertEquals(favoriteImageProperties.resX.value(), x);
        assertEquals(favoriteImageProperties.resY.value(), y);
        assertEquals(favoriteImageProperties.createdAt.getTime(), createAtTime);
        assertEquals(favoriteImageProperties.userRealName.value(), userRealName);
        assertEquals(favoriteImageProperties.userProfileUrl.value(), userProfileUrl);
        assertEquals(favoriteImageProperties.userPortfolioUrl.value(), userPortfolioUrl);
        assertEquals(favoriteImageProperties.serviceName.value(), serviceName);
        assertEquals(favoriteImageProperties.serviceUrl.value(), serviceUrl);


        // Flickr specific data
        String serverId = "3123";
        String farmId = "4";
        String secret = "31243535";
        // Urls constructed from Flickr's photo url template using the data above.
        imageStreamUrl = "https://farm4.staticflickr.com/3123/116_31243535_c.jpg";
        imageDetailUrl = "https://farm4.staticflickr.com/3123/116_31243535_h.jpg";
        imageSetAsUrl = "https://farm4.staticflickr.com/3123/116_31243535_h.jpg";
        imageShareUrl = "https://flic.kr/p/" + FlickrBaseEncoder.encode(Long.valueOf(id));
        Image flickrImage = _createFlickImage(id, serverId, farmId, secret);
        assertEquals(unsplashImage.getId().value(), id);
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

        Properties flickrImageProperties = flickrImage.getProperties();
        assertEquals(flickrImageProperties.serviceName.value(), "Flickr");
        assertEquals(flickrImageProperties.serviceUrl.value(), "https://flickr.com");
    }

    public static Image _createUnsplashImage(
            String id,
            int x,
            int y,
            long createdAtTime,
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
                new Name(userRealName),
                new Url(userProfileUrl),
                new Url(userPortfolioUrl),
                new Url(rawImageUrl),
                new Url(imageShareUrl)
        );
    }

    public static FlickrImage _createFlickImage(
            String id,
            String serverId,
            String farmId,
            String secret
    ) {
        return new FlickrImage(
                new Id(id),
                new Id(serverId),
                new Id(farmId),
                new Id(secret)
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
                new Name(userRealName),
                new Url(userProfileUrl),
                new Url(userPortfolioUrl),
                new Name(serviceName),
                new Url(serviceUrl)
        );
    }

    private static class Unsplash {

        private static final String IMAGE_STREAM_URL_OPTIONS = "?q=80&fm=jpg&w=600&fit=max";

        private static final String IMAGE_DETAIL_URL_OPTIONS =
                "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

        private static final String SET_AS_URL_OPTIONS = "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

    }

}
