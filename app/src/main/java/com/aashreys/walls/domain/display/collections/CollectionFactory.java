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

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import javax.inject.Inject;

/**
 * Created by aashreys on 04/02/17.
 */

public class CollectionFactory {

    @Inject
    public CollectionFactory() {}

    public Collection create(@Collection.Type String type, Id id, Name name) {
        switch (type) {
            case Collection.Type.UNSPLASH_COLLECTION:
                return new UnsplashCollection(id, name);

            case Collection.Type.DISCOVER:
                return new DiscoverCollection();

            case Collection.Type.FAVORITE:
                return new FavoriteCollection();
        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
