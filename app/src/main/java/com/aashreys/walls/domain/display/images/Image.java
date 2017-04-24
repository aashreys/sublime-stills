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

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.Service;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Color;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 19/03/17.
 */

public interface Image extends Parcelable {

    int RES_ORIGINAL = Integer.MAX_VALUE;

    @NonNull
    Id getId();

    @NonNull
    Url getUrl(int width);

    @NonNull
    Url getShareUrl();

    @Nullable
    Name getTitle();

    @Nullable
    Date getUploadDate();

    @NonNull
    Service getService();

    @Nullable
    Resolution getResolution();

    @Nullable
    User getUser();

    @Nullable
    Exif getExif();

    @Nullable
    Location getLocation();

    @Nullable
    Color getBackgroundColor();

    @Type
    @NonNull
    String getType();

    @interface Type {

        String FLICKR = "image_type_flickr";

        String UNSPLASH = "image_type_unsplash";

    }

}
