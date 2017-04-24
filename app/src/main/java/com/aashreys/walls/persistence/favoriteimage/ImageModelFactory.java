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

package com.aashreys.walls.persistence.favoriteimage;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.persistence.models.ImageModel;
import com.aashreys.walls.persistence.models.UnsplashImageModel;

import javax.inject.Inject;

/**
 * Created by aashreys on 23/04/17.
 */

public class ImageModelFactory {

    @Inject
    public ImageModelFactory() {}
    
    public ImageModel create(Image image) {
        if (image instanceof UnsplashImage) {
            return new UnsplashImageModel((UnsplashImage) image);
        }
        throw new IllegalArgumentException("Unknown image type");
    }
    
}
