package com.aashreys.walls;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImageImpl;
import com.aashreys.walls.domain.display.images.ImageValidator;
import com.aashreys.walls.domain.display.sources.ImageValidatorImpl;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class ImageTests extends BaseTestCase {

    @Test
    public void test_image_constructor_args() {
        // Constructing image with valid arguments
        String serverId = "32";
        Integer x = 35;
        Integer y = 55;
        long date = 253543;
        String photographerName = "Ivan";
        String userProfileUrl = "http://userprofile.com";
        String userPortfolioUrl = "http://userportfolio.com";
        String imageSmallUrl = "http://image.smallurl.com";
        String imageRegularUrl = "http://image.regularurl.com";
        String imageFullUrl = "http://image.fullurl.com";
        String serviceName = "Bazooka";
        String serviceUrl = "http://bazooka.com";
        Image image = _createImage(
                serverId,
                x,
                y,
                date,
                photographerName,
                userProfileUrl,
                userPortfolioUrl,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                serviceName,
                serviceUrl
        );
        assertEquals(image.serverId().value(), serverId);
        assertEquals(image.resX().value(), x);
        assertEquals(image.resY().value(), y);
        assertEquals(image.createdAt().getTime(), date);
        assertEquals(image.userRealName().value(), photographerName);
        assertEquals(image.userProfileUrl().value(), userProfileUrl);
        assertEquals(image.userPortfolioUrl().value(), userPortfolioUrl);
        assertEquals(image.smallImageUrl().value(), imageSmallUrl);
        assertEquals(image.regularImageUrl().value(), imageRegularUrl);
        assertEquals(image.fullImageUrl().value(), imageFullUrl);
        assertEquals(image.serviceName().value(), serviceName);
        assertEquals(image.serviceUrl().value(), serviceUrl);

        // Constructing image with allowed invalid arguments
        Image image2 = _createImage(
                serverId,
                -1,
                -1,
                date,
                null,
                null,
                null,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                serviceName,
                null
        );
        assertEquals(image2.serverId().value(), serverId);
        assertEquals(image2.resX(), null);
        assertEquals(image2.resY(), null);
        assertEquals(image2.createdAt().getTime(), date);
        assertEquals(image2.userRealName(), null);
        assertEquals(image2.userProfileUrl(), null);
        assertEquals(image2.userPortfolioUrl(), null);
        assertEquals(image2.smallImageUrl().value(), imageSmallUrl);
        assertEquals(image2.regularImageUrl().value(), imageRegularUrl);
        assertEquals(image2.fullImageUrl().value(), imageFullUrl);
        assertEquals(image2.serviceName().value(), serviceName);
        assertEquals(image2.serviceUrl(), null);
    }

    @Test
    public void test_image_validator_logic() {
        String serverId = "32";
        Integer x = 35;
        Integer y = 55;
        long date = 253543;
        String photographerName = "Ivan";
        String userProfileUrl = "http://userprofile.com";
        String userPortfolioUrl = "http://userportfolio.com";
        String imageSmallUrl = "http://image.smallurl.com";
        String imageRegularUrl = "http://image.regularurl.com";
        String imageFullUrl = "http://image.fullurl.com";
        String serviceName = "Bazooka";
        String serviceUrl = "http://bazooka.com";

        // Constructing image with allowed invalid arguments
        Image image = _createImage(
                serverId,
                -1,
                -1,
                date,
                null,
                null,
                null,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                serviceName,
                null
        );

        ImageValidator validator = new ImageValidatorImpl();
        assertEquals(validator.isValid(image), true);

        // Constructing image with invalid arguments
        Image image2 = _createImage(
                "",
                -1,
                -1,
                date,
                null,
                null,
                null,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                null,
                null
        );
        assertEquals(validator.isValid(image2), false);
    }

    private Image _createImage(
            String serverId,
            int x,
            int y,
            long date,
            String photographerName,
            String userProfileUrl,
            String userPortfolioUrl,
            String imageSmallUrl,
            String imageRegularUrl,
            String imageFullUrl,
            String serviceName,
            String serviceUrl
    ) {
        return new ImageImpl(
                new ServerId(serverId),
                new Pixel(x),
                new Pixel(y),
                new Date(date),
                new Name(photographerName),
                new Url(userProfileUrl),
                new Url(userPortfolioUrl),
                new Url(imageSmallUrl),
                new Url(imageRegularUrl),
                new Url(imageFullUrl),
                new Name(serviceName),
                new Url(serviceUrl)
        );
    }

    @Test
    public void test_equals_and_hashcode() {
        // Constructing image with valid arguments
        String serverId = "32";
        Integer x = 35;
        Integer y = 55;
        long date = 253543;
        String photographerName = "Ivan";
        String userProfileUrl = "http://userprofile.com";
        String userPortfolioUrl = "http://userportfolio.com";
        String imageSmallUrl = "http://image.smallurl.com";
        String imageRegularUrl = "http://image.regularurl.com";
        String imageFullUrl = "http://image.fullurl.com";
        String serviceName = "Bazooka";
        String serviceUrl = "http://bazooka.com";

        Image image1 = _createImage(
                serverId,
                x,
                y,
                date,
                photographerName,
                userProfileUrl,
                userPortfolioUrl,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                serviceName,
                serviceUrl
        );

        Image image2 = _createImage(
                serverId,
                x,
                y,
                date,
                photographerName,
                userProfileUrl,
                userPortfolioUrl,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                serviceName,
                serviceUrl
        );

        assertEquals(image1, image2);
        assertEquals(image1.hashCode(), image2.hashCode());

        Image image3 = _createImage(
                "35354",
                x,
                y,
                date,
                photographerName,
                userProfileUrl,
                userPortfolioUrl,
                imageSmallUrl,
                imageRegularUrl,
                imageFullUrl,
                "Facebook",
                null
        );

        assertEquals(image1.equals(image3), false);
        assertEquals(image1.hashCode() == image3.hashCode(), false);
        assertEquals(image2.equals(image3), false);
        assertEquals(image2.hashCode() == image3.hashCode(), false);
    }


}
