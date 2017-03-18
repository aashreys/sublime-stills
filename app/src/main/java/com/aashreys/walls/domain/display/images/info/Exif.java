package com.aashreys.walls.domain.display.images.info;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 18/03/17.
 */
public class Exif implements Parcelable {

    public static final Creator<Exif> CREATOR = new Creator<Exif>() {
        @Override
        public Exif createFromParcel(Parcel source) {return new Exif(source);}

        @Override
        public Exif[] newArray(int size) {return new Exif[size];}
    };

    @Nullable
    public Name camera;

    @Nullable
    public Name exposureTime;

    @Nullable
    public Name aperture;

    @Nullable
    public Name focalLength;

    @Nullable
    public Name iso;

    public Exif() {}

    protected Exif(Parcel in) {
        this.camera = in.readParcelable(Name.class.getClassLoader());
        this.exposureTime = in.readParcelable(Name.class.getClassLoader());
        this.aperture = in.readParcelable(Name.class.getClassLoader());
        this.focalLength = in.readParcelable(Name.class.getClassLoader());
        this.iso = in.readParcelable(Name.class.getClassLoader());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.camera, flags);
        dest.writeParcelable(this.exposureTime, flags);
        dest.writeParcelable(this.aperture, flags);
        dest.writeParcelable(this.focalLength, flags);
        dest.writeParcelable(this.iso, flags);
    }
}
