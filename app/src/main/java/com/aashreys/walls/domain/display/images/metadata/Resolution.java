package com.aashreys.walls.domain.display.images.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import com.aashreys.walls.domain.values.Pixel;

/**
 * Created by aashreys on 19/03/17.
 */

public class Resolution implements Parcelable {

    public static final Creator<Resolution> CREATOR = new Creator<Resolution>() {
        @Override
        public Resolution createFromParcel(Parcel source) {return new Resolution(source);}

        @Override
        public Resolution[] newArray(int size) {return new Resolution[size];}
    };

    private final Pixel resX, resY;

    public Resolution(Pixel resX, Pixel resY) {
        this.resX = resX;
        this.resY = resY;
    }

    protected Resolution(Parcel in) {
        this.resX = in.readParcelable(Pixel.class.getClassLoader());
        this.resY = in.readParcelable(Pixel.class.getClassLoader());
    }

    public Pixel getResX() {
        return resX;
    }

    public Pixel getResY() {
        return resY;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resX, flags);
        dest.writeParcelable(this.resY, flags);
    }
}
