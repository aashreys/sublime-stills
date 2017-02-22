package com.aashreys.walls.domain.display.sources;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImageValidator;

/**
 * Created by aashreys on 08/02/17.
 */

public class ImageValidatorImpl implements ImageValidator {

    public ImageValidatorImpl() {}

    @Override
    public boolean isValid(Image image) {
        return image.serverId() != null && image.serverId().isValid() &&
                image.createdAt() != null &&
                image.smallImageUrl() != null && image.smallImageUrl().isValid() &&
                image.regularImageUrl() != null && image.regularImageUrl().isValid() &&
                image.fullImageUrl() != null && image.fullImageUrl().isValid();
    }
}
