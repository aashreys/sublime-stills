package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 03/12/16.
 */

public class LocalId extends Value<Long> {

    private static final String TAG = LocalId.class.getSimpleName();

    public static final Creator<LocalId> CREATOR = new Creator<LocalId>() {
        @Override
        public LocalId createFromParcel(Parcel source) {
            return new LocalId(source);
        }

        @Override
        public LocalId[] newArray(int size) {return new LocalId[size];}
    };

    public LocalId(Long value) {
        super(value);
    }

    @Override
    public boolean isValid() {
        return value() != null && value() >= 0;
    }

    protected LocalId(Parcel in) {
        super(in.readLong());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(value());
    }
}
