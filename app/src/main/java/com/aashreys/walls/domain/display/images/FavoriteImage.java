package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 03/03/17.
 */

public class FavoriteImage implements Image {

    public static final Creator<FavoriteImage> CREATOR = new Creator<FavoriteImage>() {
        @Override
        public FavoriteImage createFromParcel(Parcel source) {return new FavoriteImage(source);}

        @Override
        public FavoriteImage[] newArray(int size) {return new FavoriteImage[size];}
    };

    private final Id id;

    private final Name title;

    private final Url imageStreamUrl;

    private final Url imageDetailUrl;

    private final Url imageSetAsUrl;

    private final Url imageShareUrl;

    private final Properties properties;


    public FavoriteImage(
            Id id,
            Name title,
            Url imageStreamUrl,
            Url imageDetailUrl,
            Url imageSetAsUrl,
            Url imageShareUrl,
            Pixel resX,
            Pixel resY,
            Date createdAt,
            Name userRealName,
            Url userProfileUrl,
            Url userPortfolioUrl,
            Name serviceName,
            Url serviceUrl
    ) {
        this.id = id;
        this.title = title;
        this.imageStreamUrl = imageStreamUrl;
        this.imageDetailUrl = imageDetailUrl;
        this.imageSetAsUrl = imageSetAsUrl;
        this.imageShareUrl = imageShareUrl;
        this.properties = new Properties(serviceName);
        this.properties.title = title;
        this.properties.resX = resX;
        this.properties.resY = resY;
        this.properties.createdAt = createdAt;
        this.properties.userRealName = userRealName;
        this.properties.userProfileUrl = userProfileUrl;
        this.properties.userPortfolioUrl = userPortfolioUrl;
        this.properties.serviceUrl = serviceUrl;
    }

    protected FavoriteImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.title = in.readParcelable(Name.class.getClassLoader());
        this.imageStreamUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageDetailUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageSetAsUrl = in.readParcelable(Url.class.getClassLoader());
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
                return imageStreamUrl;

            case UrlType.IMAGE_DETAIL:
                return imageDetailUrl;

            case UrlType.SET_AS:
                return imageSetAsUrl;

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
        dest.writeParcelable(this.title, flags);
        dest.writeParcelable(this.imageStreamUrl, flags);
        dest.writeParcelable(this.imageDetailUrl, flags);
        dest.writeParcelable(this.imageSetAsUrl, flags);
        dest.writeParcelable(this.imageShareUrl, flags);
        dest.writeParcelable(this.properties, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteImage)) return false;

        FavoriteImage that = (FavoriteImage) o;

        if (!id.equals(that.id)) return false;
        return properties.serviceName.equals(that.properties.serviceName);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + properties.serviceName.hashCode();
        return result;
    }
}
