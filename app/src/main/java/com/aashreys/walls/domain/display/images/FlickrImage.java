package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.info.ImageInfo;
import com.aashreys.walls.domain.display.images.info.Location;
import com.aashreys.walls.domain.display.images.utils.FlickrBaseEncoder;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 02/03/17.
 */

public class FlickrImage implements Image {

    public static final Creator<FlickrImage> CREATOR = new Creator<FlickrImage>() {
        @Override
        public FlickrImage createFromParcel(Parcel source) {return new FlickrImage(source);}

        @Override
        public FlickrImage[] newArray(int size) {return new FlickrImage[size];}
    };

    public static final Name SERVICE_NAME = new Name("Flickr");

    public static final Url SERVICE_URL = new Url("https://flickr.com");

    // Order of args: farm_id, server_id, id, secret, size
    private static final String IMAGE_URL_TEMPLATE =
            "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    // Arg: Base58 encoded photo id
    private static final String IMAGE_SHARE_URL_TEMPLATE = "https://flic.kr/p/%s/";

    // Arg: User id
    private static final String USER_PROIFLE_URL_TEMPLATE =
            "https://www.flickr.com/people/%s/";

    // Arg: User id
    private static final String USER_PHOTOSTREAM_URL_TEMPLATE =
            "https://www.flickr.com/photos/%s/";

    private final Id id;

    private final Id serverId;

    private final Id farmId;

    private final Id secret;

    private final ImageInfo info;

    public FlickrImage(
            Id id,
            Id ownerId,
            Id serverId,
            Id farmId,
            Id secret,
            Name title,
            Name userName,
            Date createdAt,
            Location location
    ) {
        this.id = id;
        this.serverId = serverId;
        this.farmId = farmId;
        this.secret = secret;
        info = new ImageInfo(SERVICE_NAME);
        info.title = title;
        info.userRealName = userName;
        info.createdAt = createdAt;
        info.userId = ownerId;
        info.userProfileUrl = new Url(String.format(USER_PROIFLE_URL_TEMPLATE, ownerId.value()));
        info.userPortfolioUrl = new Url(String.format(
                USER_PHOTOSTREAM_URL_TEMPLATE,
                ownerId.value()
        ));
        info.serviceUrl = SERVICE_URL;
        info.location = location;
    }

    private FlickrImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.serverId = in.readParcelable(Id.class.getClassLoader());
        this.farmId = in.readParcelable(Id.class.getClassLoader());
        this.secret = in.readParcelable(Id.class.getClassLoader());
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
                return new Url(String.format(
                        IMAGE_URL_TEMPLATE,
                        farmId.value(),
                        serverId.value(),
                        id.value(),
                        secret.value(),
                        "c"
                ));

            case UrlType.IMAGE_DETAIL:
            case UrlType.SET_AS:
                return new Url(String.format(
                        IMAGE_URL_TEMPLATE,
                        farmId.value(),
                        serverId.value(),
                        id.value(),
                        secret.value(),
                        "h"
                ));

            case UrlType.SHARE:
                return new Url(String.format(
                        IMAGE_SHARE_URL_TEMPLATE,
                        FlickrBaseEncoder.encode(Long.valueOf(id.value()))
                ));

            default:
                throw new IllegalArgumentException("Unexpected type value");
        }
    }

    @NonNull
    public ImageInfo getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlickrImage)) return false;

        FlickrImage that = (FlickrImage) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.serverId, flags);
        dest.writeParcelable(this.farmId, flags);
        dest.writeParcelable(this.secret, flags);
        dest.writeParcelable(this.info, flags);
    }
}
