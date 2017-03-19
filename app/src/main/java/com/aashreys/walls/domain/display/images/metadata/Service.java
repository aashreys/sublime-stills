package com.aashreys.walls.domain.display.images.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 19/03/17.
 */

public class Service implements Parcelable {

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel source) {return new Service(source);}

        @Override
        public Service[] newArray(int size) {return new Service[size];}
    };

    @NonNull
    private final Name name;

    @Nullable
    private final Url url;

    public Service(@NonNull Name name, Url url) {
        this.name = name;
        this.url = url;
    }

    protected Service(Parcel in) {
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.url = in.readParcelable(Url.class.getClassLoader());
    }

    @NonNull
    public Name getName() {
        return name;
    }

    @Nullable
    public Url getUrl() {
        return url;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.name, flags);
        dest.writeParcelable(this.url, flags);
    }
}
