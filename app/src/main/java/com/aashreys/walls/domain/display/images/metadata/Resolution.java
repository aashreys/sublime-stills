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

    private final Pixel width, height;

    public Resolution(Pixel width, Pixel height) throws InstantiationException {
        this.width = width;
        this.height = height;
        validate();
    }

    protected Resolution(Parcel in) {
        this.width = in.readParcelable(Pixel.class.getClassLoader());
        this.height = in.readParcelable(Pixel.class.getClassLoader());
    }

    public Pixel getWidth() {
        return width;
    }

    public Pixel getHeight() {
        return height;
    }

    private boolean isValid() {
        return width != null && width.isValid() && width.value() > 0 && height != null &&
                height.isValid() && height.value() > 0;
    }

    private void validate() throws InstantiationException {
        if (!isValid()) {
            throw new InstantiationException("Unable to create resolution");
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.width, flags);
        dest.writeParcelable(this.height, flags);
    }
}
