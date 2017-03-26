/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.Service;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.utils.FlickrBaseEncoder;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by aashreys on 19/03/17.
 */

public class FlickrImage implements Image {

    public static final Creator<FlickrImage> CREATOR = new Creator<FlickrImage>() {
        @Override
        public FlickrImage createFromParcel(Parcel source) {return new FlickrImage(source);}

        @Override
        public FlickrImage[] newArray(int size) {return new FlickrImage[size];}
    };

    private static final String TAG = FlickrImage.class.getSimpleName();

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

    protected static final Service SERVICE = new Service(
            new Name("Flickr"),
            new Url("https://flickr.com")
    );

    private static final TreeMap<Integer, Character> SIZE_MAP = new TreeMap<>();

    static {
        SIZE_MAP.put(240, 'm');
        SIZE_MAP.put(320, 'n');
        SIZE_MAP.put(500, '-');
        SIZE_MAP.put(640, 'z');
        SIZE_MAP.put(800, 'c');
        SIZE_MAP.put(1024, 'b');
        SIZE_MAP.put(1600, 'h');
        SIZE_MAP.put(2048, 'k');
        SIZE_MAP.put(Integer.MAX_VALUE, 'o');
    }

    @NonNull
    private final Id id;

    @NonNull
    private final Id serverId;

    @NonNull
    private final Id farmId;

    @NonNull
    private final Id secret;

    @Nullable
    private final Name title;

    @NonNull
    private final User user;

    @Nullable
    private final Date uploadDate;

    @Nullable
    private final Location location;

    @Nullable
    private Exif exif;

    @Nullable
    private Resolution resolution;

    public FlickrImage(
            @NonNull Id id,
            @NonNull Id ownerId,
            @NonNull Id serverId,
            @NonNull Id farmId,
            @NonNull Id secret,
            Name title,
            Name userName,
            Date uploadDate,
            Location location
    ) {
        this.id = id;
        this.serverId = serverId;
        this.farmId = farmId;
        this.secret = secret;
        this.title = title;
        this.uploadDate = uploadDate;
        this.user = new User(
                ownerId,
                userName,
                new Url(String.format(USER_PROIFLE_URL_TEMPLATE, ownerId.value())),
                new Url(String.format(USER_PHOTOSTREAM_URL_TEMPLATE, ownerId.value()))
        );
        this.location = location;
    }

    protected FlickrImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.serverId = in.readParcelable(Id.class.getClassLoader());
        this.farmId = in.readParcelable(Id.class.getClassLoader());
        this.secret = in.readParcelable(Id.class.getClassLoader());
        this.title = in.readParcelable(Name.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        long tmpUploadDate = in.readLong();
        this.uploadDate = tmpUploadDate == -1 ? null : new Date(tmpUploadDate);
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.exif = in.readParcelable(Exif.class.getClassLoader());
        this.resolution = in.readParcelable(Resolution.class.getClassLoader());
    }

    @NonNull
    @Override
    public Id getId() {
        return id;
    }

    @NonNull
    @Override
    public Url getUrl(int width) {
        return new Url(String.format(
                IMAGE_URL_TEMPLATE,
                farmId.value(),
                serverId.value(),
                id.value(),
                secret.value(),
                SIZE_MAP.get(SIZE_MAP.ceilingKey(width))
        ));
    }

    @NonNull
    @Override
    public Url getShareUrl() {
        return new Url(String.format(
                IMAGE_SHARE_URL_TEMPLATE,
                FlickrBaseEncoder.encode(Long.valueOf(id.value()))
        ));
    }

    @Nullable
    @Override
    public Name getTitle() {
        return title;
    }

    @Nullable
    @Override
    public Date getUploadDate() {
        return uploadDate;
    }

    @NonNull
    @Override
    public Service getService() {
        return SERVICE;
    }

    @Nullable
    @Override
    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(@Nullable Resolution resolution) {
        this.resolution = resolution;
    }

    @Nullable
    @Override
    public User getUser() {
        return user;
    }

    @Nullable
    @Override
    public Exif getExif() {
        return exif;
    }

    public void setExif(@Nullable Exif exif) {
        this.exif = exif;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @NonNull
    @Override
    public String getType() {
        return Type.FLICKR;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.serverId, flags);
        dest.writeParcelable(this.farmId, flags);
        dest.writeParcelable(this.secret, flags);
        dest.writeParcelable(this.title, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.uploadDate != null ? this.uploadDate.getTime() : -1);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.exif, flags);
        dest.writeParcelable(this.resolution, flags);
    }
}
