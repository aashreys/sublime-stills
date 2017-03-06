package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 02/12/16.
 */

public class Pixel extends Value<Integer> {

    public static final Creator<Pixel> CREATOR = new Creator<Pixel>() {
        @Override
        public Pixel createFromParcel(Parcel source) {
            return new Pixel(source);
        }

        @Override
        public Pixel[] newArray(int size) {return new Pixel[size];}
    };

    public Pixel(Integer value) {
        super(value);
    }

    @Override
    public boolean isValid() {
        return value() != null &&  value() >= 0;
    }

    private Pixel(Parcel in) {
        super(in.readInt());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value());
    }
}
