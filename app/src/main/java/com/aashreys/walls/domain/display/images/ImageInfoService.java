package com.aashreys.walls.domain.display.images;

import com.aashreys.walls.domain.display.images.info.ImageInfo;

/**
 * Created by aashreys on 05/03/17.
 */

public interface ImageInfoService {

    /**
     * Sets values for various data members of an {@link Image}'s {@link ImageInfo}. The image
     * with property data is delivered via {@link Listener#onComplete(Image)}. The caller still
     * null check the {@link ImageInfo} data members since not all properties may apply to all
     * image types.
     *
     * @param image image to set properties on
     * @param listener listener to delivery results to
     */
    void addInfo(Image image, Listener listener);

    interface Listener {

        void onComplete(Image imageWithProperties);

    }

}
