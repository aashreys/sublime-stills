package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;

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

    private static final Name serviceName = new Name("Unsplash");

    private static final Url serviceUrl = new Url("https://unsplash.com");

    // Valid fields
    @NonNull private final Id id;

    @NonNull private final Url rawImageUrl;

    @NonNull private final Url imageShareUrl;

    private final Properties properties;

    public UnsplashImage(
            @NonNull Id id,
            @NonNull Pixel resX,
            @NonNull Pixel resY,
            @NonNull Date createdAt,
            @NonNull Name userRealName,
            @NonNull Url userProfileUrl,
            @NonNull Url userPortfolioUrl,
            @NonNull Url rawImageUrl,
            @NonNull Url imageShareUrl
    ) {
        this.id = id;
        this.rawImageUrl = rawImageUrl;
        this.imageShareUrl = imageShareUrl;
        this.properties = new Properties(serviceName);
        this.properties.resX = resX;
        this.properties.resY = resY;
        this.properties.createdAt = createdAt;
        this.properties.userRealName = userRealName;
        this.properties.userProfileUrl = userProfileUrl;
        this.properties.userPortfolioUrl = userPortfolioUrl;
        this.properties.serviceUrl = serviceUrl;
    }

    private UnsplashImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.rawImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageShareUrl = in.readParcelable(Url.class.getClassLoader());
        this.properties = in.readParcelable(Properties.class.getClassLoader());
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
    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.rawImageUrl, flags);
        dest.writeParcelable(this.imageShareUrl, flags);
        dest.writeParcelable(this.properties, flags);
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
