package com.aashreys.walls.domain.display.collections;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.aashreys.walls.domain.display.collections.Collection.Type.FAVORITE;
import static com.aashreys.walls.domain.display.collections.Collection.Type.UNSPLASH_COLLECTION;
import static com.aashreys.walls.domain.display.collections.Collection.Type.UNSPLASH_RECENT;

/**
 * Created by aashreys on 04/02/17.
 */

public interface Collection<T extends Source> extends Parcelable {

    @NonNull
    ServerId id();

    @NonNull
    Name name();

    boolean isRemovable();

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UNSPLASH_RECENT,
            UNSPLASH_COLLECTION,
            FAVORITE
    })
    @interface Type {

        String UNSPLASH_RECENT     = "type_unsplash_recent";
        String UNSPLASH_COLLECTION = "type_unsplash_collection";
        String FAVORITE            = "type_favorite";

    }

}
