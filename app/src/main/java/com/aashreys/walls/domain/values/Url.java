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
