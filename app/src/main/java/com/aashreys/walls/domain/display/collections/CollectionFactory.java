package com.aashreys.walls.domain.display.collections;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 04/02/17.
 */

public class CollectionFactory {

    public CollectionFactory() {}

    public Collection create(@Collection.Type String type, Id id, Name name) {
        switch (type) {
            case Collection.Type.UNSPLASH_RECENT:
                return new UnsplashRecentCollection();

            case Collection.Type.UNSPLASH_COLLECTION:
                return new UnsplashCollection(id, name);

            case Collection.Type.DISCOVER:
                return new DiscoverCollection();

            case Collection.Type.FAVORITE:
                return new FavoriteCollection();

            case Collection.Type.FLICKR_TAG:
                return new FlickrTag(name);

            case Collection.Type.FLICKR_RECENT:
                return new FlickrRecentCollection();
        }
        throw new IllegalArgumentException("Unexpected collection type");
    }

}
