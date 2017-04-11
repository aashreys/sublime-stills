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

package com.aashreys.walls.ui.views;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by aashreys on 09/04/17.
 */

class ChipViewModel implements ViewModel {

    private EventCallback eventCallback;

    private boolean isChecked;

    private Collection collection;

    @Nullable
    private ChipView.OnCheckedListener listener;

    @DimenRes
    int getHorizontalPaddingRes() {
        return R.dimen.spacing_medium;
    }

    @DimenRes
    int getVerticalPaddingRes() {
        return R.dimen.spacing_small;
    }

    @ColorRes
    int getCheckedTextColorRes() {
        return R.color.white;
    }

    @ColorRes
    int getUncheckedTextColorRes() {
        return R.color.textColorPrimary;
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
        return R.drawable.chip_background_dark;
    }

    @DrawableRes
    int getUncheckedBackgroundDrawableRes() {
        return R.drawable.chip_background_light;
    }

    boolean isChecked() {
        return isChecked;
    }

    void setChecked(boolean checked) {
        isChecked = checked;
    }

    void setCollection(Collection collection) {
        this.collection = collection;
        if (eventCallback != null) {
            eventCallback.onCollectionSet();
        }

    }

    String getCollectionName() {
        return collection != null ? WordUtils.capitalizeFully(collection.getName().value()) : null;
    }

    void onChipClicked() {
        if (collection != null) {
            if (isChecked()) {
                notifyDeselection(collection);
                setChecked(false);
                if (eventCallback != null) {
                    eventCallback.onUnchecked();
                }
            } else {
                notifySelection(collection);
                setChecked(true);
                if (eventCallback != null) {
                    eventCallback.onChecked();
                }
            }
        }
    }

    private void notifySelection(Collection collection) {
        if (listener != null) {
            listener.onChipViewChecked(collection);
        }
    }

    private void notifyDeselection(Collection collection) {
        if (listener != null) {
            listener.onChipViewUnchecked(collection);
        }
    }

    void setOnCheckedListener(@Nullable ChipView.OnCheckedListener listener) {
        this.listener = listener;
    }

    void setEventCallback(EventCallback callback) {
        this.eventCallback = callback;
    }

    interface EventCallback {

        void onCollectionSet();

        void onChecked();

        void onUnchecked();

    }
}
