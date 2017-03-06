package com.aashreys.walls.ui.helpers;

import android.support.annotation.DrawableRes;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;

/**
 * Created by aashreys on 03/03/17.
 */

public class CollectionUiHelper {

    @DrawableRes
    public static int getIconForCollectionType(@Collection.Type String type) {
        switch (type) {
            case Collection.Type.FAVORITE:
                return R.drawable.ic_favorite_black_24dp;
            default:
                return R.drawable.ic_unsplash_logo_black_24dp;
        }
    }

}
