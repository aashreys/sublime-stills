package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 03/03/17.
 */

public class UnsplashRecentCollection implements Collection {

    public static final Creator<UnsplashRecentCollection> CREATOR =
            new Creator<UnsplashRecentCollection>() {
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

    private static final Id ID = new Id("1");

    private static final Name NAME = new Name("Unsplash");

    public UnsplashRecentCollection() {}

    protected UnsplashRecentCollection(Parcel in) {}

    @NonNull
    @Override
    public Id getId() {
        return ID;
    }

    @NonNull
    @Override
    public Name getName() {
        return NAME;
    }

    @Override
    public String getType() {
        return Type.UNSPLASH_RECENT;
    }

    @Override
    public boolean isRemovable() {
        return true;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnsplashRecentCollection)) {
            return false;
        }
        return true;
    }

}
