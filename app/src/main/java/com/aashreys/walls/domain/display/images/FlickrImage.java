package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.utils.FlickrBaseEncoder;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

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

    // Order of args: farm_id, server_id, id, secret, size
    private static final String URL_TEMPLATE =
            "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    // Arg: Base58 encoded id
    private static final String SHARE_URL_TEMPLATE = "https://flic.kr/p/%s/";

    public static final Name SERVICE_NAME = new Name("Flickr");

    public static final Url SERVICE_URL = new Url("https://flickr.com");

    private final Id id;

    private final Id serverId;

    private final Id farmId;

    private final Id secret;

    private final Properties properties;

    public FlickrImage(Id id, Id ownerId, Id serverId, Id farmId, Id secret) {
        this.id = id;
        this.serverId = serverId;
        this.farmId = farmId;
        this.secret = secret;
        properties = new Properties(SERVICE_NAME);
        properties.userId = ownerId;
        properties.serviceUrl = SERVICE_URL;

    }

    private FlickrImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.serverId = in.readParcelable(Id.class.getClassLoader());
        this.farmId = in.readParcelable(Id.class.getClassLoader());
        this.secret = in.readParcelable(Id.class.getClassLoader());
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
                return new Url(String.format(
                        URL_TEMPLATE,
                        farmId.value(),
                        serverId.value(),
                        id.value(),
                        secret.value(),
                        "c"
                ));

            case UrlType.IMAGE_DETAIL:
            case UrlType.SET_AS:
                return new Url(String.format(
                        URL_TEMPLATE,
                        farmId.value(),
                        serverId.value(),
                        id.value(),
                        secret.value(),
                        "h"
                ));

            case UrlType.SHARE:
                return new Url(String.format(
                        SHARE_URL_TEMPLATE,
                        FlickrBaseEncoder.encode(Long.valueOf(id.value()))
                ));

            default:
                throw new IllegalArgumentException("Unexpected type value");
        }
    }

    @NonNull
    @Override
    public Properties getProperties() {
        return properties;
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
        dest.writeParcelable(this.properties, flags);
    }
}
