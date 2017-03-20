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

package com.aashreys.walls.domain.values;

import android.os.Parcel;

/**
 * Created by aashreys on 03/12/16.
 */

public class Id extends Value<String> {

    public static final Creator<Id> CREATOR = new Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel source) {
            return new Id(source);
        }

        @Override
        public Id[] newArray(int size) {return new Id[size];}
    };

    @Override
    public boolean isValid() {
        return value() != null && !value().isEmpty();
    }

    public Id(String value) {
        super(value);
    }

    protected Id(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value());
    }
}
