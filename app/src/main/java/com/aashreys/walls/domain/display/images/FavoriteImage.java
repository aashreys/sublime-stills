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

    private final ImageInfo info;


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
            Id userId,
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
        this.info = new ImageInfo(serviceName);
        this.info.title = title;
        this.info.resX = resX;
        this.info.resY = resY;
        this.info.createdAt = createdAt;
        this.info.userId = userId;
        this.info.userRealName = userRealName;
        this.info.userProfileUrl = userProfileUrl;
        this.info.userPortfolioUrl = userPortfolioUrl;
        this.info.serviceUrl = serviceUrl;
    }

    protected FavoriteImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.title = in.readParcelable(Name.class.getClassLoader());
        this.imageStreamUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageDetailUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageSetAsUrl = in.readParcelable(Url.class.getClassLoader());
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
    public ImageInfo getInfo() {
        return info;
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
        dest.writeParcelable(this.info, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteImage)) return false;

        FavoriteImage that = (FavoriteImage) o;

        if (!id.equals(that.id)) return false;
        return info.serviceName.equals(that.info.serviceName);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + info.serviceName.hashCode();
        return result;
    }
}
