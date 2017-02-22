package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 01/02/17.
 */
public final class ImageImpl implements Image {

    public static final Creator<ImageImpl> CREATOR
            = new Creator<ImageImpl>() {
        public ImageImpl createFromParcel(Parcel in) {
            return new ImageImpl(in);
        }

        public ImageImpl[] newArray(int size) {
            return new ImageImpl[size];
        }
    };

    // Valid fields
    @NonNull private final ServerId vServerId;
    @NonNull private final Date     vCreatedAt;
    @NonNull private final Url      vSmallImageUrl;
    @NonNull private final Url      vRegularImageUrl;
    @NonNull private final Url      vFullImageUrl;
    @NonNull private final Name     vServiceName;

    @Nullable private final Pixel resX, resY;
    @Nullable private final Name photographerName;
    @Nullable private final Url  userProfileUrl;
    @Nullable private final Url  userPortfolioUrl;
    @Nullable private final Url  serviceUrl;

    public ImageImpl(
            @NonNull ServerId serverId,
            @NonNull Pixel resX,
            @NonNull Pixel resY,
            @NonNull Date createdAt,
            @NonNull Name photographerName,
            @NonNull Url userProfileUrl,
            @NonNull Url userPortfolioUrl,
            @NonNull Url smallImageUrl,
            @NonNull Url regularImageUrl,
            @NonNull Url fullImageUrl,
            @NonNull Name serviceName,
            @NonNull Url serviceUrl
    ) {
        this.vServerId = serverId;
        this.resX = resX.isValid() ? resX : null;
        this.resY = resY.isValid() ? resY : null;
        this.vCreatedAt = createdAt;
        this.photographerName = photographerName.isValid() ? photographerName : null;
        this.userProfileUrl = userProfileUrl.isValid() ? userProfileUrl : null;
        this.userPortfolioUrl = userPortfolioUrl.isValid() ? userPortfolioUrl : null;
        this.vSmallImageUrl = smallImageUrl;
        this.vRegularImageUrl = regularImageUrl;
        this.vFullImageUrl = fullImageUrl;
        this.vServiceName = serviceName;
        this.serviceUrl = serviceUrl.isValid() ? serviceUrl : null;
    }

    private ImageImpl(Parcel in) {
        this.vServerId = in.readParcelable(ServerId.class.getClassLoader());
        this.resX = in.readParcelable(Pixel.class.getClassLoader());
        this.resY = in.readParcelable(Pixel.class.getClassLoader());
        this.vCreatedAt = new Date(in.readLong());
        this.photographerName = in.readParcelable(Name.class.getClassLoader());
        this.userProfileUrl = in.readParcelable(Url.class.getClassLoader());
        this.userPortfolioUrl = in.readParcelable(Url.class.getClassLoader());
        this.vSmallImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.vRegularImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.vFullImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.vServiceName = in.readParcelable(Name.class.getClassLoader());
        this.serviceUrl = in.readParcelable(Url.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(vServerId, flags);
        parcel.writeParcelable(resX, flags);
        parcel.writeParcelable(resY, flags);
        parcel.writeLong(vCreatedAt.getTime());
        parcel.writeParcelable(photographerName, flags);
        parcel.writeParcelable(userProfileUrl, flags);
        parcel.writeParcelable(userPortfolioUrl, flags);
        parcel.writeParcelable(vSmallImageUrl, flags);
        parcel.writeParcelable(vRegularImageUrl, flags);
        parcel.writeParcelable(vFullImageUrl, flags);
        parcel.writeParcelable(vServiceName, flags);
        parcel.writeParcelable(serviceUrl, flags);
    }

    @Override
    public int hashCode() {
        int result = vServerId.hashCode();
        result = 31 * result + vServiceName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ImageImpl)) { return false; }

        ImageImpl image = (ImageImpl) o;

        if (!vServerId.equals(image.vServerId)) { return false; }
        return vServiceName.equals(image.vServiceName);

    }

    @NonNull
    @Override
    public ServerId serverId() {
        return vServerId;
    }

    @Override
    public Pixel resX() {
        return resX;
    }

    @Override
    public Pixel resY() {
        return resY;
    }

    @NonNull
    @Override
    public Date createdAt() {
        return vCreatedAt;
    }

    @Nullable
    @Override
    public Name userRealName() {
        return photographerName;
    }

    @Nullable
    @Override
    public Url userProfileUrl() {
        return userProfileUrl;
    }

    @Nullable
    @Override
    public Url userPortfolioUrl() {
        return userPortfolioUrl;
    }

    @NonNull
    @Override
    public Url smallImageUrl() {
        return vSmallImageUrl;
    }

    @NonNull
    @Override
    public Url regularImageUrl() {
        return vRegularImageUrl;
    }

    @NonNull
    @Override
    public Url fullImageUrl() {
        return vFullImageUrl;
    }

    @NonNull
    @Override
    public Name serviceName() {
        return vServiceName;
    }

    @Nullable
    @Override
    public Url serviceUrl() {
        return serviceUrl;
    }

}
