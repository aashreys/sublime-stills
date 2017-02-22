package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 02/12/16.
 */

public class Name extends Value<String> {

    private static final String TAG = Name.class.getSimpleName();

    public static final Creator<Name> CREATOR = new Creator<Name>() {
        @Override
        public Name createFromParcel(Parcel source) {
            return new Name(source);
        }

        @Override
        public Name[] newArray(int size) {return new Name[size];}
    };

    public Name(String value) {
        super(value);
    }

    @Override
    public boolean isValid() {
        return value() != null && !value().equals("null") && !value().isEmpty();
    }

    private Name(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value());
    }
}
