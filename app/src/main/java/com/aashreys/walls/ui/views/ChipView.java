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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.ui.helpers.UiHelper;

/**
 * Created by aashreys on 04/02/17.
 */
public class ChipView extends AppCompatTextView implements ChipViewModel.EventCallback {

    private ChipViewModel viewModel;

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
        viewModel = new ChipViewModel();
        int paddingVertical =
                getResources().getDimensionPixelSize(viewModel.getVerticalPaddingRes());
        int paddingHorizontal =
                getResources().getDimensionPixelSize(viewModel.getHorizontalPaddingRes());
        setHeight(getResources().getDimensionPixelSize(viewModel.getHeightRes()));
        setTextColor(UiHelper.getColor(getContext(), viewModel.getUncheckedTextColorRes()));
        setGravity(viewModel.getGravity());
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        setBackgroundResource(viewModel.getUncheckedBackgroundDrawableRes());
        viewModel.setEventCallback(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onChipClicked();
            }
        });
    }

    public void setCollection(final Collection collection) {
        viewModel.setCollection(collection);
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        viewModel.setOnCheckedListener(listener);
    }

    public void setChecked(boolean isChecked) {
        viewModel.setChecked(isChecked);
    }

    @Override
    public void onCollectionChanged(String collectionName) {
        setText(collectionName);
    }

    @Override
    public void onChecked() {
        setBackgroundResource(viewModel.getCheckedBackgroundDrawableRes());
        setTextColor(UiHelper.getColor(getContext(), viewModel.getCheckedTextColorRes()));
    }

    @Override
    public void onUnchecked() {
        setBackgroundResource(viewModel.getUncheckedBackgroundDrawableRes());
        setTextColor(UiHelper.getColor(getContext(), viewModel.getUncheckedTextColorRes()));
    }

    public interface OnCheckedListener {

        void onChipViewChecked(Collection checkedCollection);

        void onChipViewUnchecked(Collection uncheckedCollection);

    }
}
