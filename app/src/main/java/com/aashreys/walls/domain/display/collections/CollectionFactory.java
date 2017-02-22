package com.aashreys.walls.domain.display.collections;

import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;

/**
 * Created by aashreys on 04/02/17.
 */

public class CollectionFactory {

    public CollectionFactory() {}

    public Collection create(@Collection.Type String type, ServerId id, Name name) {
        switch (type) {
            case Collection.Type.UNSPLASH_COLLECTION:
                return new UnsplashCollection(id, name);

            case Collection.Type.UNSPLASH_RECENT:
                return new UnsplashRecentCollection();

            case Collection.Type.FAVORITE:
                return new FavoriteCollection();

        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
