package com.aashreys.walls.domain.values;

import android.os.Parcelable;

/**
 * Created by aashreys on 02/12/16.
 */

abstract class Value<T> implements Parcelable {

    private static final String TAG = Value.class.getSimpleName();

    private final T value;

    protected Value(T value) {
        this.value = value;
    }

    public boolean isValid() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().isAssignableFrom(getClass()))) return false;
        return value().equals(((Value<T>) o).value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public T value() {
        return value;
    }
}
