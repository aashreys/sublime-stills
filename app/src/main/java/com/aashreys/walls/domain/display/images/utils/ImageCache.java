package com.aashreys.walls.domain.display.images.utils;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 05/03/17.
 */

public interface ImageCache {

    void add(Image image);

    void remove(Image image);

    Image get(Name imageServiceName, Id imageId);

}
