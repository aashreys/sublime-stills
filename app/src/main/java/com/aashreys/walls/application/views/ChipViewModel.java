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

package com.aashreys.walls.application.views;

import android.support.annotation.AttrRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;

import org.apache.commons.lang3.text.WordUtils;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/04/17.
 */

class ChipViewModel implements ViewModel {

    private EventCallback eventCallback;

    private boolean isChecked;

    private Collection collection;

    @Nullable
    private ChipView.OnCheckedListener listener;

    @Inject
    public ChipViewModel() {}

    @DimenRes
    int getHorizontalPaddingRes() {
        return R.dimen.spacing_medium;
    }

    @DimenRes
    int getVerticalPaddingRes() {
        return R.dimen.spacing_small;
    }

    @AttrRes
    int getCheckedTextColorAttrRes() {
        return android.R.attr.textColorPrimaryInverse;
    }

    @AttrRes
    int getUncheckedTextColorAttrRes() {
        return android.R.attr.textColorPrimary;
    }

    @DimenRes
    int getHeightRes() {
        return R.dimen.height_small;
    }

    int getGravity() {
        return Gravity.CENTER;
    }

    @DrawableRes
    int getCheckedBackgroundDrawableRes() {
        return R.drawable.chip_background_checked;
    }

    @DrawableRes
    int getUncheckedBackgroundDrawableRes() {
        return R.drawable.chip_background_unchecked;
    }

    boolean isChecked() {
        return isChecked;
    }

    void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            onCheckedChanged();
        }
    }

    void setCollection(Collection collection) {
        this.collection = collection;
        if (eventCallback != null) {
            eventCallback.onCollectionChanged(WordUtils.capitalizeFully(collection.getName().value()));
        }
    }

    void onChipClicked() {
        if (collection != null) {
            setChecked(!isChecked());
            onCheckedChanged();
        }
    }

    private void onCheckedChanged() {
        if (isChecked()) {
            if (eventCallback != null) {
                eventCallback.onChecked();
            }
            if (collection != null && listener != null) {
                listener.onChipViewChecked(collection);
            }
        } else {
            if (eventCallback != null) {
                eventCallback.onUnchecked();
            }
            if (collection != null && listener != null) {
                listener.onChipViewUnchecked(collection);
            }
        }
    }

    void setOnCheckedListener(@Nullable ChipView.OnCheckedListener listener) {
        this.listener = listener;
    }

    void setEventCallback(EventCallback callback) {
        this.eventCallback = callback;
    }

    interface EventCallback {

        void onCollectionChanged(String collectionName);

        void onChecked();

        void onUnchecked();

    }
}
