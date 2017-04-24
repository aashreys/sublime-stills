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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.InstantiationException;
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

    public Service(@NonNull Name name, @Nullable Url url) throws InstantiationException {
        this.name = name;
        this.url = url;
        validate();
    }

    protected Service(Parcel in) {
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.url = in.readParcelable(Url.class.getClassLoader());
    }

    private boolean isValid() {
        return name != null && name.isValid();
    }

    private void validate() throws InstantiationException {
        if (!isValid()) {
            throw new InstantiationException("Unable to create Service");
        }
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

    public static Service createConstant(String name, String url) {
        try {
            return new Service(new Name(name), new Url(url));
        } catch (InstantiationException e) {
            return null;
        }
    }
}
