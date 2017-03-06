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
import static com.aashreys.walls.domain.display.collections.Collection.Type.FLICKR_RECENT;
import static com.aashreys.walls.domain.display.collections.Collection.Type.FLICKR_TAG;
import static com.aashreys.walls.domain.display.collections.Collection.Type.UNSPLASH_COLLECTION;
import static com.aashreys.walls.domain.display.collections.Collection.Type.UNSPLASH_RECENT;

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
            UNSPLASH_RECENT,
            UNSPLASH_COLLECTION,
            FLICKR_RECENT,
            FLICKR_TAG
    })

    @interface Type {

        String DISCOVER            = "type_discover";
        String FAVORITE            = "type_favorite";
        String UNSPLASH_RECENT     = "type_unsplash_recent";
        String UNSPLASH_COLLECTION = "type_unsplash_collection";
        String FLICKR_RECENT       = "type_flickr_recent";
        String FLICKR_TAG          = "type_flickr_tag";

    }

    boolean isRemovable();

}
