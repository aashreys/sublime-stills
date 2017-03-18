package com.aashreys.walls.domain.display.images.info;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 18/03/17.
 */

public class Location implements Parcelable {

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {return new Location(source);}

        @Override
        public Location[] newArray(int size) {return new Location[size];}
    };

    @Nullable
    private final Name name;

    private final double longitude;

    private final double latitude;

    public Location(Name name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Location(Parcel in) {
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    @Nullable
    public Name getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.name, flags);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        if (Double.compare(location.longitude, longitude) != 0) return false;
        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (name == null && location.name == null) {
            return true;
        } else {
            return name.equals(location.name);
        }

    }

    @Override
    public int hashCode() {
        int result = 1;
        long temp;
        if (name != null) {
            result = name.hashCode();
        }
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
