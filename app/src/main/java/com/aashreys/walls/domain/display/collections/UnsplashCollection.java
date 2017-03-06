package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 04/02/17.
 */

public class UnsplashCollection implements Collection, Parcelable {

    public static final Parcelable.Creator<UnsplashCollection> CREATOR
            = new Parcelable.Creator<UnsplashCollection>() {
        @Override
        public UnsplashCollection createFromParcel(Parcel source) {
            return new UnsplashCollection(source);
        }

        @Override
        public UnsplashCollection[] newArray(int size) {
            return new UnsplashCollection[size];
        }
    };

    private final Id id;

    private final Name name;

    public UnsplashCollection(Id id, Name name) {
        this.id = id;
        this.name = name;
    }


    private UnsplashCollection(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.name = in.readParcelable(Name.class.getClassLoader());
    }

    @NonNull
    @Override
    public Id getId() {
        return id;
    }

    @NonNull
    @Override
    public Name getName() {
        return name;
    }

    @Override
    public String getType() {
        return Type.UNSPLASH_COLLECTION;
    }

    @Override
    public boolean isRemovable() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnsplashCollection)) {
            return false;
        }

        UnsplashCollection that = (UnsplashCollection) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.name, flags);
    }

}
