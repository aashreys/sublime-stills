package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.info.ImageInfo;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 01/02/17.
 */
public final class UnsplashImage implements Image {

    public static final Creator<UnsplashImage> CREATOR = new Creator<UnsplashImage>() {
        @Override
        public UnsplashImage createFromParcel(Parcel source) {return new UnsplashImage(source);}

        @Override
        public UnsplashImage[] newArray(int size) {return new UnsplashImage[size];}
    };

    private static final String IMAGE_STREAM_URL_OPTIONS = "?q=80&fm=jpg&w=600&fit=max";

    private static final String IMAGE_DETAIL_URL_OPTIONS =
            "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

    private static final String SET_AS_URL_OPTIONS = "?q=80&cs=tinysrgb&fm=jpg&w=1080&fit=max";

    public static final Name SERVICE_NAME = new Name("Unsplash");

    public static final Url SERVICE_URL = new Url("https://unsplash.com");

    // Valid fields
    @NonNull private final Id id;

    @NonNull private final Url rawImageUrl;

    @NonNull private final Url imageShareUrl;

    private final ImageInfo info;

    public UnsplashImage(
            @NonNull Id id,
            @NonNull Pixel resX,
            @NonNull Pixel resY,
            @NonNull Date createdAt,
            @NonNull Id userId,
            @NonNull Name userRealName,
            @NonNull Url userProfileUrl,
            @NonNull Url userPortfolioUrl,
            @NonNull Url rawImageUrl,
            @NonNull Url imageShareUrl
    ) {
        this.id = id;
        this.rawImageUrl = rawImageUrl;
        this.imageShareUrl = imageShareUrl;
        this.info = new ImageInfo(SERVICE_NAME);
        this.info.resX = resX;
        this.info.resY = resY;
        this.info.createdAt = createdAt;
        this.info.userId = userId;
        this.info.userRealName = userRealName;
        this.info.userProfileUrl = userProfileUrl;
        this.info.userPortfolioUrl = userPortfolioUrl;
        this.info.serviceUrl = SERVICE_URL;
    }

    private UnsplashImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.rawImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageShareUrl = in.readParcelable(Url.class.getClassLoader());
        this.info = in.readParcelable(ImageInfo.class.getClassLoader());
    }

    @NonNull
    @Override
    public Id getId() {
        return id;
    }

    @NonNull
    @Override
    public Url getUrl(@UrlType int type) {
        switch (type) {
            case UrlType.IMAGE_STREAM:
                return rawImageUrl.append(IMAGE_STREAM_URL_OPTIONS);

            case UrlType.IMAGE_DETAIL:
                return rawImageUrl.append(IMAGE_DETAIL_URL_OPTIONS);

            case UrlType.SET_AS:
                return rawImageUrl.append(SET_AS_URL_OPTIONS);

            case UrlType.SHARE:
                return imageShareUrl;
        }
        throw new IllegalArgumentException("Unexpected type value");
    }

    @NonNull
    public ImageInfo getInfo() {
        return info;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.rawImageUrl, flags);
        dest.writeParcelable(this.imageShareUrl, flags);
        dest.writeParcelable(this.info, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsplashImage)) return false;

        UnsplashImage that = (UnsplashImage) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
