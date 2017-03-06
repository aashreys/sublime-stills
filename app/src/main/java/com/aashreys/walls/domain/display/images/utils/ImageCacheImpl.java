package com.aashreys.walls.domain.display.images.utils;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by aashreys on 05/03/17.
 */

public class ImageCacheImpl implements ImageCache {

    private final Map<String, Image> imageMap;

    public ImageCacheImpl() {
        imageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Image image) {
        imageMap.put(_getKey(image.getProperties().serviceName, image.getId()), image);
    }

    @Override
    public void remove(Image image) {
        imageMap.remove(_getKey(image.getProperties().serviceName, image.getId()));
    }

    @Override
    public Image get(Name imageServiceName, Id imageId) {
        if (imageServiceName != null && imageId != null) {
            return imageMap.get(_getKey(imageServiceName, imageId));
        } else {
            return null;
        }
    }

    private String _getKey(Name imageServiceName, Id imageId) {
        return imageServiceName.value() + imageId.value();
    }
}
