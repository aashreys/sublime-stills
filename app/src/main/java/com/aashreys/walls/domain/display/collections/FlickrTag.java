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

package com.aashreys.walls.domain.display.collections;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

/**
 * Created by aashreys on 02/03/17.
 */

public class FlickrTag implements Collection {

    public static final Creator<FlickrTag> CREATOR = new Creator<FlickrTag>() {
        @Override
        public FlickrTag createFromParcel(Parcel source) {
            return new FlickrTag(source);
        }

        @Override
        public FlickrTag[] newArray(int size) {return new FlickrTag[size];}
    };

    private final Id id;

    private final Name name;

    public FlickrTag(Name tag) {
        this.id = new Id(tag.value());
        this.name = tag;
    }

    protected FlickrTag(Parcel in) {
        this.id = in.readParcelable(Id.class.getClassLoader());
        this.name = in.readParcelable(Name.class.getClassLoader());
    }

    @NonNull
    @Override
    public Id getId() {
        return id;
    }

    @NonNull
    @Override
    public Name getName() {
        return name;
    }

    @Override
    public String getType() {
        return Type.FLICKR_TAG;
    }

    @Override
    public boolean isRemovable() {
        return true;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.name, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlickrTag)) return false;

        FlickrTag that = (FlickrTag) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
