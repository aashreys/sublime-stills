package com.aashreys.walls.domain.display.images;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.Service;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 19/03/17.
 */

public interface Image extends Parcelable {

    public static final int RES_ORIGINAL = Integer.MAX_VALUE;

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

    @Type
    @NonNull
    String getType();

    public @interface Type {

        String FLICKR = "image_type_flickr";

        String UNSPLASH = "image_type_unsplash";

    }

}
