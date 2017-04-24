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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.application.helpers.UiHelper;

import javax.inject.Inject;

/**
 * Created by aashreys on 11/03/17.
 */

public class HintView extends FrameLayout implements HintViewModel.EventListener {

    @Inject HintViewModel viewModel;

    private TextView hintText;

    private ImageButton closeButton;

    public HintView(Context context) {
        super(context);
        _init(context, null);
    }

    public HintView(
            Context context,
            @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        _init(context, attrs);
    }

    public HintView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs);
    }

    private void _init(Context context, @Nullable AttributeSet attrs) {
        UiHelper.getUiComponent(context).inject(this);
        viewModel.setEventListener(this);
        LayoutInflater.from(context).inflate(R.layout.layout_view_hint, this, true);

        hintText = (TextView) findViewById(R.id.text_hint);
        closeButton = (ImageButton) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onCloseButtonClicked();
            }
        });

        final String hintString, seenKeyString;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HintView, 0, 0);
        try {
            hintString = a.getString(R.styleable.HintView_hv_hint);
            seenKeyString = a.getString(R.styleable.HintView_hv_seen_key);
        } finally {
            a.recycle();
        }

        viewModel.setHint(hintString);
        viewModel.setSeenKey(seenKeyString);
    }

    @Override
    public void onHintSet() {
        hintText.setText(viewModel.getHint());
    }

    @Override
    public void onHintDismissed() {
        setVisibility(GONE);
    }
}
