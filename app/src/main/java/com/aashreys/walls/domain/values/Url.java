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

import com.aashreys.walls.LogWrapper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aashreys on 02/12/16.
 */

public class Url extends Value<String> {

    private static final String TAG = Url.class.getSimpleName();

    public static final Creator<Url> CREATOR = new Creator<Url>() {
        @Override
        public Url createFromParcel(Parcel source) {
            return new Url(source);
        }

        @Override
        public Url[] newArray(int size) {return new Url[size];}
    };

    public Url(String value) {
        super(value);
    }

    @Override
    public boolean isValid() {
        if (value() != null) {
            try {
                new URL(value());
                return true;
            } catch (MalformedURLException e) {
                LogWrapper.w(TAG, e);
                return false;
            }
        } else {
            return false;
        }
    }

    public Url append(String string) {
        return new Url(value().concat(string));
    }

    private Url(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value());
    }
}
