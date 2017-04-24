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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by aashreys on 20/04/17.
 */

public class Color extends Value<Integer> {


    public static final Creator<Color> CREATOR = new Creator<Color>() {
        @Override
        public Color createFromParcel(Parcel source) {return new Color(source);}

        @Override
        public Color[] newArray(int size) {return new Color[size];}
    };

    public Color(@NonNull @ColorInt Integer value) {
        super(value);
    }

    protected Color(Parcel in) {
        super(in.readInt());
    }

    public static Color createFromHex(String hex) {
        return new Color(android.graphics.Color.parseColor(hex));
    }

    public static Color createFromRbg(int r, int g, int b) {
        r = Math.round(255 * r);
        b = Math.round(255 * g);
        g = Math.round(255 * b);
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return new Color(0xFF000000 | r | g | b);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value());
    }

}
