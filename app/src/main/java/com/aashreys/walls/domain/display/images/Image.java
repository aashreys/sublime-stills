package com.aashreys.walls.domain.display.images;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 21/11/16.
 */

public interface Image extends Parcelable {

    @NonNull
    ServerId serverId();

    @Nullable
    Pixel resX();

    @Nullable
    Pixel resY();

    @NonNull
    Date createdAt();

    @Nullable
    Name userRealName();

    @Nullable
    Url userProfileUrl();

    @Nullable
    Url userPortfolioUrl();

    @NonNull
    Url smallImageUrl();

    @NonNull
    Url regularImageUrl();

    @NonNull
    Url fullImageUrl();

    @NonNull
    Name serviceName();

    @Nullable
    Url serviceUrl();

}
