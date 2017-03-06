package com.aashreys.walls.domain.display.images;

import static com.aashreys.walls.domain.display.images.Image.Properties;

/**
 * Created by aashreys on 05/03/17.
 */

public interface ImagePropertiesService {

    /**
     * Sets values for various data members of an {@link Image}'s {@link Properties}. The image
     * with property data is delivered via {@link Listener#onComplete(Image)}. The caller still
     * null check the {@link Properties} data members since not all properties may apply to all
     * image types.
     *
     * @param image image to set properties on
     * @param listener listener to delivery results to
     */
    void addProperties(Image image, Listener listener);

    interface Listener {

        void onComplete(Image imageWithProperties);

    }

}
