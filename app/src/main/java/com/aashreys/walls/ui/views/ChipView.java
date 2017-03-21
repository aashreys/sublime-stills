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

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by aashreys on 04/02/17.
 */
public class ChipView extends AppCompatTextView {

    @Nullable
    private OnSelectedListener listener;

    private boolean isChecked;

    public ChipView(Context context) {
        super(context);
        _init(context);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    private void _init(Context context) {
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen
                .spacing_medium);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        setGravity(Gravity.CENTER);
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        setBackgroundResource(R.drawable.chip_background_light);
    }

    public void setCollection(final Collection collection) {
        setText(WordUtils.capitalizeFully(collection.getName().value()));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked()) {
                    notifyListener(null, null);
                    setChecked(false);
                } else {
                    notifyListener(ChipView.this, collection);
                    setChecked(true);
                }
            }
        });
    }

    private void notifyListener(ChipView chipView, Collection collection) {
        if (listener != null) {
            listener.onChipViewSelected(chipView, collection);
        }
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            setBackgroundResource(R.drawable.chip_background_dark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextColor(getResources().getColor(R.color.white, null));
            } else {
                setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            setBackgroundResource(R.drawable.chip_background_light);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextColor(getResources().getColor(R.color.black, null));
            } else {
                setTextColor(getResources().getColor(R.color.black));
            }
        }
        this.isChecked = isChecked;
    }

    public void resetState() {
        setText(null);
    }

    public interface OnSelectedListener {

        void onChipViewSelected(
                @Nullable ChipView selectedChipView,
                @Nullable Collection selectedCollection
        );

    }
}
