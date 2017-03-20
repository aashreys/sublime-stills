/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        imageMap.put(_getKey(image.getService().getName(), image.getId()), image);
    }

    @Override
    public void remove(Image image) {
        imageMap.remove(_getKey(image.getService().getName(), image.getId()));
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
