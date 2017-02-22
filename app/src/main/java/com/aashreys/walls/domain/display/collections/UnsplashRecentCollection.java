package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.sources.UnsplashRecentSource;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;

/**
 * Created by aashreys on 04/02/17.
 */

public class UnsplashRecentCollection implements Collection<UnsplashRecentSource>, Parcelable {

    public static final Parcelable.Creator<UnsplashRecentCollection> CREATOR
            = new Parcelable.Creator<UnsplashRecentCollection>() {
        @Override
        public UnsplashRecentCollection createFromParcel(Parcel source) {
            return new UnsplashRecentCollection(source);
        }

        @Override
        public UnsplashRecentCollection[] newArray(int size) {
            return new
                    UnsplashRecentCollection[size];
        }
    };

    private static final ServerId ID   = new ServerId("1");
    private static final Name     NAME = new Name("Discover");

    public UnsplashRecentCollection() {}

    private UnsplashRecentCollection(Parcel in) {}

    @NonNull
    @Override
    public ServerId id() {
        return ID;
    }

    @NonNull
    @Override
    public Name name() {
        return NAME;
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof UnsplashRecentCollection)) { return false; }
        return true;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

}
