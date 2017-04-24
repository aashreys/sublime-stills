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

import com.aashreys.walls.domain.InstantiationException;

/**
 * Created by aashreys on 23/04/17.
 */

public class Coordinates implements Parcelable {

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel source) {return new Coordinates(source);}

        @Override
        public Coordinates[] newArray(int size) {return new Coordinates[size];}
    };

    private final double latitude;

    private final double longitude;

    public Coordinates(double latitude, double longitude) throws InstantiationException {
        this.latitude = latitude;
        this.longitude = longitude;
        validate();
    }

    protected Coordinates(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    private void validate() throws InstantiationException {
        if (!isValid()) {
            throw new InstantiationException("Unable to create Coordinates");
        }
    }

    private boolean isValid() {
        return latitude != 0 || longitude != 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }
}
