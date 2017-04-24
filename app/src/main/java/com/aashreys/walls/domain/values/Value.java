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

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by aashreys on 02/12/16.
 */

public abstract class Value<T> implements Parcelable {

    private static final String TAG = Value.class.getSimpleName();

    private final T value;

    protected Value(@NonNull T value) {
        this.value = value;
    }

    public boolean isValid() {
        return value() != null;
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

    @Nullable
    public static <T> T getValidValue(@Nullable Value<T> value) {
        return getValidValue(value, null);
    }

    @Nullable
    public static <T> T getValidValue(@Nullable Value<T> value, T defValue) {
        return value != null && value.isValid() ? value.value() : defValue;
    }
}
