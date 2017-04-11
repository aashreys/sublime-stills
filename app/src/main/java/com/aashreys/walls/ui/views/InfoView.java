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
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashreys.walls.R;

/**
 * Created by aashreys on 17/03/17.
 */

public class InfoView extends LinearLayout implements InfoViewModel.EventListener {

    private TextView titleText;

    private TextView infoText;

    private ImageView titleIcon;

    private InfoViewModel viewModel;

    public InfoView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    public InfoView(
            Context context, @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public InfoView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        viewModel = new InfoViewModel();
        viewModel.setEventListener(this);
        setGravity(viewModel.getGravity());
        LayoutInflater.from(context).inflate(R.layout.layout_info_view, this, true);

        titleText = (TextView) findViewById(R.id.text_key);
        titleIcon = (ImageView) findViewById(R.id.icon_key);
        infoText = (TextView) findViewById(R.id.text_value);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InfoView,
                0, 0
        );

        int iconKeyRes;
        String keyString, infoString;
        try {
            iconKeyRes = a.getResourceId(R.styleable.InfoView_iv_key_icon, 0);
            keyString = a.getString(R.styleable.InfoView_iv_key_string);
            infoString = a.getString(R.styleable.InfoView_iv_value);
        } finally {
            a.recycle();
        }

        if (iconKeyRes != 0) {
            viewModel.setTitleIcon(iconKeyRes);
        } else if (keyString != null) {
            viewModel.setTitle(keyString);
        }
        viewModel.setInfo(infoString);
    }

    public void setInfoWithIcon(@DrawableRes int titleIcon, String info) {
        viewModel.setTitleIcon(titleIcon);
        viewModel.setInfo(info);
    }

    public void setInfo(String title, String info) {
        viewModel.setTitle(title);
        viewModel.setInfo(info);
    }

    @Override
    public void onTitleSet(String title) {
        titleIcon.setVisibility(GONE);
        titleText.setVisibility(VISIBLE);
        titleText.setText(title);
    }

    @Override
    public void onTitleIconSet(@DrawableRes int icon) {
        titleText.setVisibility(GONE);
        titleIcon.setVisibility(VISIBLE);
        titleIcon.setImageResource(icon);
    }

    @Override
    public void onInfoSet(String info) {
        infoText.setText(info);
    }
}
