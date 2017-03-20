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
