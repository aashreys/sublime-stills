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
 * Created by aashreys on 03/03/17.
 */

public class UnsplashRecentCollection implements Collection {

    public static final Creator<UnsplashRecentCollection> CREATOR =
            new Creator<UnsplashRecentCollection>() {
                @Override
                public UnsplashRecentCollection createFromParcel(Parcel source) {
                    return new UnsplashRecentCollection(source);
                }

                @Override
                public UnsplashRecentCollection[] newArray(int size) {
                    return new
                            UnsplashRecentCollection[size];
                }
            };

    private static final Id ID = new Id("1");

    private static final Name NAME = new Name("Unsplash");

    public UnsplashRecentCollection() {}

    protected UnsplashRecentCollection(Parcel in) {}

    @NonNull
    @Override
    public Id getId() {
        return ID;
    }

    @NonNull
    @Override
    public Name getName() {
        return NAME;
    }

    @Override
    public String getType() {
        return Type.UNSPLASH_RECENT;
    }

    @Override
    public boolean isRemovable() {
        return true;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof UnsplashRecentCollection;
    }

}
