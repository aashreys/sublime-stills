package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 03/12/16.
 */

public class Id extends Value<String> {

    public static final Creator<Id> CREATOR = new Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel source) {
            return new Id(source);
        }

        @Override
        public Id[] newArray(int size) {return new Id[size];}
    };

    @Override
    public boolean isValid() {
        return value() != null && !value().isEmpty();
    }

    public Id(String value) {
        super(value);
    }

    protected Id(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value());
    }
}
