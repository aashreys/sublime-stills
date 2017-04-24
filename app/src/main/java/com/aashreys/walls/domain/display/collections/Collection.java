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

package com.aashreys.walls.domain.display.collections;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.aashreys.walls.domain.display.collections.Collection.Type.DISCOVER;
import static com.aashreys.walls.domain.display.collections.Collection.Type.FAVORITE;
import static com.aashreys.walls.domain.display.collections.Collection.Type.UNSPLASH_COLLECTION;

/**
 * Created by aashreys on 04/02/17.
 */

public interface Collection extends Parcelable {

    @NonNull
    Id getId();

    @NonNull
    Name getName();

    @Type
    String getType();

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            DISCOVER,
            FAVORITE,
            UNSPLASH_COLLECTION,
    })

    @interface Type {

        String DISCOVER            = "type_discover";
        String FAVORITE            = "type_favorite";
        String UNSPLASH_COLLECTION = "type_unsplash_collection";

    }

    boolean isRemovable();

}
