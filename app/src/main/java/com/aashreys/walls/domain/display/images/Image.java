package com.aashreys.walls.domain.display.images;

import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.info.ImageInfo;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 02/03/17.
 */

public interface Image extends Parcelable {

    @NonNull
    Id getId();

    @NonNull
    Url getUrl(@UrlType int type);

    @NonNull
    ImageInfo getInfo();

    @IntDef
    @interface UrlType {

        int IMAGE_STREAM = 1;

        int IMAGE_DETAIL = 2;

        int SHARE = 3;

        int SET_AS = 4;

    }

}
