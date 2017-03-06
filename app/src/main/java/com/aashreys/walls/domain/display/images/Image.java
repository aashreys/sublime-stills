package com.aashreys.walls.domain.display.images;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;

import java.util.Date;

/**
 * Created by aashreys on 02/03/17.
 */

public interface Image extends Parcelable {

    @NonNull
    Id getId();

    @NonNull
    Url getUrl(@UrlType int type);

    @NonNull
    Properties getProperties();

    @IntDef
    @interface UrlType {

        int IMAGE_STREAM = 1;

        int IMAGE_DETAIL = 2;

        int SHARE = 3;

        int SET_AS = 4;

    }

    /**
     * Created by aashreys on 02/03/17.
     */

    class Properties implements Parcelable {

        public static final Creator<Properties> CREATOR = new Creator<Properties>() {
            @Override
            public Properties createFromParcel(Parcel source) {return new Properties(source);}

            @Override
            public Properties[] newArray(int size) {return new Properties[size];}
        };

        @NonNull
        public final Name serviceName;

        @Nullable
        public Name title;

        @Nullable
        public Pixel resX, resY;

        @Nullable
        public Date createdAt;

        @Nullable
        public Id userId;

        @Nullable
        public Name userRealName;

        @Nullable
        public Url userProfileUrl;

        @Nullable
        public Url userPortfolioUrl;

        @Nullable
        public Url serviceUrl;

        protected Properties(@NonNull Name serviceName) {
            this.serviceName = serviceName;
        }

        protected Properties(Parcel in) {
            this.title = in.readParcelable(Name.class.getClassLoader());
            this.resX = in.readParcelable(Pixel.class.getClassLoader());
            this.resY = in.readParcelable(Pixel.class.getClassLoader());
            long tmpCreatedAt = in.readLong();
            this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
            this.userId = in.readParcelable(Id.class.getClassLoader());
            this.userRealName = in.readParcelable(Name.class.getClassLoader());
            this.userProfileUrl = in.readParcelable(Url.class.getClassLoader());
            this.userPortfolioUrl = in.readParcelable(Url.class.getClassLoader());
            this.serviceName = in.readParcelable(Name.class.getClassLoader());
            this.serviceUrl = in.readParcelable(Url.class.getClassLoader());
        }

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.title, flags);
            dest.writeParcelable(this.resX, flags);
            dest.writeParcelable(this.resY, flags);
            dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
            dest.writeParcelable(this.userId, flags);
            dest.writeParcelable(this.userRealName, flags);
            dest.writeParcelable(this.userProfileUrl, flags);
            dest.writeParcelable(this.userPortfolioUrl, flags);
            dest.writeParcelable(this.serviceName, flags);
            dest.writeParcelable(this.serviceUrl, flags);
        }
    }
}
