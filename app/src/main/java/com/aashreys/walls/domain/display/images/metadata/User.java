package com.aashreys.walls.domain.display.images.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 19/03/17.
 */

public class User implements Parcelable {

    private static final String TAG = User.class.getSimpleName();

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {return new User(source);}

        @Override
        public User[] newArray(int size) {return new User[size];}
    };

    @NonNull
    private final Id id;

    @Nullable
    private final Name name;

    @Nullable
    private final Url profileUrl;

    @Nullable
    private final Url portfolioUrl;

    public User(
            @NonNull Id id,
            Name name,
            Url profileUrl,
            Url portfolioUrl
    ) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
        this.portfolioUrl = portfolioUrl;
    }

    protected User(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.profileUrl = in.readParcelable(Url.class.getClassLoader());
        this.portfolioUrl = in.readParcelable(Url.class.getClassLoader());
    }

    @NonNull
    public Id getId() {
        return id;
    }

    @Nullable
    public Name getName() {
        return name;
    }

    @Nullable
    public Url getProfileUrl() {
        return profileUrl;
    }

    @Nullable
    public Url getPortfolioUrl() {
        return portfolioUrl;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.name, flags);
        dest.writeParcelable(this.profileUrl, flags);
        dest.writeParcelable(this.portfolioUrl, flags);
    }
}
