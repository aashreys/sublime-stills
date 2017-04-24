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

package com.aashreys.walls.persistence.collections;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.persistence.models.CollectionModel;
import com.aashreys.walls.persistence.models.DiscoverCollectionModel;
import com.aashreys.walls.persistence.models.FavoriteCollectionModel;
import com.aashreys.walls.persistence.models.UnsplashCollectionModel;

import javax.inject.Inject;

/**
 * Created by aashreys on 23/04/17.
 */

public class CollectionModelFactory {

    @Inject
    public CollectionModelFactory() {}

    public CollectionModel create(Collection collection) {
        if (collection instanceof DiscoverCollection) {
            return new DiscoverCollectionModel();
        }
        if (collection instanceof FavoriteCollection) {
            return new FavoriteCollectionModel();
        }
        if (collection instanceof UnsplashCollection) {
            return new UnsplashCollectionModel((UnsplashCollection) collection);
        }
        throw new IllegalArgumentException("Unknown collection type");
    }

}
