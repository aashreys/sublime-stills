package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 02/03/17.
 */

public class FlickrRecentCollection implements Collection {

    public static final Creator<FlickrRecentCollection> CREATOR =
            new Creator<FlickrRecentCollection>() {
                @Override
                public FlickrRecentCollection createFromParcel(Parcel source) {
                    return new FlickrRecentCollection(source);
                }

                @Override
                public FlickrRecentCollection[] newArray(int size) {
                    return new
                            FlickrRecentCollection[size];
                }
            };

    private static final Id ID = new Id("1");

    private static final Name NAME = new Name("Flickr");

    public FlickrRecentCollection() {}

    protected FlickrRecentCollection(Parcel in) {}

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
        return Type.FLICKR_RECENT;
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
        return o instanceof FlickrRecentCollection;
    }

}
