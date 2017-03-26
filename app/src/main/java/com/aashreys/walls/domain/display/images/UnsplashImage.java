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
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 19/03/17.
 */

public class UnsplashImage implements Image {

    static final Service SERVICE = new Service(
            new Name("Unsplash"),
            new Url("https://unsplash.com")
    );

    public static final Creator<UnsplashImage> CREATOR = new Creator<UnsplashImage>() {
        @Override
        public UnsplashImage createFromParcel(Parcel source) {return new UnsplashImage(source);}

        @Override
        public UnsplashImage[] newArray(int size) {return new UnsplashImage[size];}
    };

    private static final String IMAGE_URL_CONFIG = "?q=75&cs=tinysrgb&fm=jpg&w=%s&fit=max";

    private final Id id;

    private final Url rawImageUrl;

    private final Url imageShareUrl;

    private final User user;

    private final Resolution resolution;

    private final Date createdAt;

    private Location location;

    private Exif exif;

    private Name title;

    public UnsplashImage(
            @NonNull Id id,
            @Nullable Resolution resolution,
            @NonNull Date createdAt,
            @Nullable User user,
            @NonNull Url rawImageUrl,
            @NonNull Url imageShareUrl
    ) {
        this.id = id;
        this.rawImageUrl = rawImageUrl;
        this.imageShareUrl = imageShareUrl;
        this.resolution = resolution;
        this.user = user;
        this.createdAt = createdAt;
    }

    protected UnsplashImage(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.rawImageUrl = in.readParcelable(Url.class.getClassLoader());
        this.imageShareUrl = in.readParcelable(Url.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.resolution = in.readParcelable(Resolution.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.exif = in.readParcelable(Exif.class.getClassLoader());
        this.title = in.readParcelable(Name.class.getClassLoader());
    }

    @NonNull
    @Override
    public Id getId() {
        return id;
    }

    @NonNull
    @Override
    public Url getUrl(int width) {
        if (width != RES_ORIGINAL) {
            return rawImageUrl.append(String.format(IMAGE_URL_CONFIG, width));
        } else {
            return rawImageUrl;
        }
    }

    @NonNull
    @Override
    public Url getShareUrl() {
        return imageShareUrl;
    }

    @Nullable
    @Override
    public Name getTitle() {
        return title;
    }

    public void setTitle(Name title) {
        this.title = title;
    }

    @Nullable
    @Override
    public Date getUploadDate() {
        return createdAt;
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

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String getType() {
        return Type.UNSPLASH;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.rawImageUrl, flags);
        dest.writeParcelable(this.imageShareUrl, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.resolution, flags);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.exif, flags);
        dest.writeParcelable(this.title, flags);
    }
}
