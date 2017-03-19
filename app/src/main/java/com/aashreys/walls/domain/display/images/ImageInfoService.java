package com.aashreys.walls.domain.display.images;

/**
 * Created by aashreys on 05/03/17.
 */

public interface ImageInfoService {

    /**
     * Sets values for various metadata members of an {@link Image}. The image with property data
     * is delivered via {@link Listener#onComplete(Image)}. The caller should still null check
     * image data since not all properties may apply to all image types.
     *
     * @param image    image to set properties on
     * @param listener listener to delivery results to
     */
    void addInfo(Image image, Listener listener);

    interface Listener {

        void onComplete(Image imageWithProperties);

    }

}
