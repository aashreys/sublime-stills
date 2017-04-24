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

import com.aashreys.walls.domain.InstantiationException;
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

    @Nullable
    private final Coordinates coordinates;


    public Location(@Nullable Name name, @Nullable Coordinates coordinates) throws
            InstantiationException {
        this.name = name;
        this.coordinates = coordinates;
        validate();

    }

    protected Location(Parcel in) {
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.coordinates = in.readParcelable(Coordinates.class.getClassLoader());
    }

    private void validate() throws InstantiationException {
        if (!isValid()) {
            throw new InstantiationException("Unable to create location");
        }
    }

    private boolean isValid() {
        return name != null && name.isValid() || coordinates != null;
    }

    @Nullable
    public Name getName() {
        return name;
    }

    @Nullable
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        if (name != null ? !name.equals(location.name) : location.name != null) return false;
        return coordinates != null ?
                coordinates.equals(location.coordinates) :
                location.coordinates == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.name, flags);
        dest.writeParcelable(this.coordinates, flags);
    }
}
