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

package com.aashreys.walls.persistence.models;

import android.support.annotation.Nullable;

import com.aashreys.walls.DontObfuscate;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.values.Value;

/**
 * Created by aashreys on 23/04/17.
 */

@DontObfuscate
public class ExifModel {

    @Nullable
    private final String camera;

    @Nullable
    private final String exposureTime;

    @Nullable
    private final String aperture;

    @Nullable
    private final String focalLength;

    @Nullable
    private final String iso;

    public ExifModel(Exif exif) {
        this.camera = Value.getValidValue(exif.camera);
        this.exposureTime = Value.getValidValue(exif.exposureTime);
        this.aperture = Value.getValidValue(exif.aperture);
        this.focalLength = Value.getValidValue(exif.focalLength);
        this.iso = Value.getValidValue(exif.iso);
    }

    @Nullable
    public String getCamera() {
        return camera;
    }

    @Nullable
    public String getExposureTime() {
        return exposureTime;
    }

    @Nullable
    public String getAperture() {
        return aperture;
    }

    @Nullable
    public String getFocalLength() {
        return focalLength;
    }

    @Nullable
    public String getIso() {
        return iso;
    }
}
