package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 04/02/17.
 */

public class DiscoverCollection implements Collection, Parcelable {

    public static final Parcelable.Creator<DiscoverCollection> CREATOR
            = new Parcelable.Creator<DiscoverCollection>() {
        @Override
        public DiscoverCollection createFromParcel(Parcel source) {
            return new DiscoverCollection(source);
        }

        @Override
        public DiscoverCollection[] newArray(int size) {
            return new
                    DiscoverCollection[size];
        }
    };

    private static final Id ID = new Id("1");

    private static final Name NAME = new Name("Discover");

    public DiscoverCollection() {}

    private DiscoverCollection(Parcel in) {}

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
        return Type.DISCOVER;
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
        if (this == o) {
            return true;
        }
        return o instanceof DiscoverCollection;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

}
