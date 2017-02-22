package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 03/12/16.
 */

public class ServerId extends Value<String> {

    public static final Creator<ServerId> CREATOR = new Creator<ServerId>() {
        @Override
        public ServerId createFromParcel(Parcel source) {
            return new ServerId(source);
        }

        @Override
        public ServerId[] newArray(int size) {return new ServerId[size];}
    };

    @Override
    public boolean isValid() {
        return value() != null && !value().isEmpty();
    }

    public ServerId(String value) {
        super(value);
    }

    protected ServerId(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value());
    }
}
