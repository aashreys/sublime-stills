/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aashreys.walls.domain.display.images.metadata;

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

    public Exif() {

    }

    public Exif(
            @Nullable Name camera,
            @Nullable Name exposureTime,
            @Nullable Name aperture,
            @Nullable Name focalLength,
            @Nullable Name iso
    ) {
        this.camera = camera;
        this.exposureTime = exposureTime;
        this.aperture = aperture;
        this.focalLength = focalLength;
        this.iso = iso;
    }

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
