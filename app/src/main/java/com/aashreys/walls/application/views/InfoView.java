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
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.application.helpers.UiHelper;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/03/17.
 */

public class InfoView extends LinearLayout implements InfoViewModel.EventListener {

    @Inject InfoViewModel viewModel;

    private TextView infoText;

    private ImageView infoIcon;

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
        UiHelper.getUiComponent(context).inject(this);
        viewModel.setEventListener(this);
        setGravity(viewModel.getGravity());
        LayoutInflater.from(context).inflate(R.layout.layout_info_view, this, true);

        infoIcon = (ImageView) findViewById(R.id.icon_key);
        infoText = (TextView) findViewById(R.id.text_value);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InfoView,
                0, 0
        );

        final int iconKeyRes;
        final String infoString;
        try {
            iconKeyRes = a.getResourceId(R.styleable.InfoView_iv_icon, 0);
            infoString = a.getString(R.styleable.InfoView_iv_value);
        } finally {
            a.recycle();
        }

        viewModel.setInfo(new Info(iconKeyRes, infoString));
    }

    public void setInfo(Info info) {
        viewModel.setInfo(info);
    }

    @Override
    public void onInfoSet() {
        infoIcon.setImageResource(viewModel.getIcon());
        infoText.setText(viewModel.getInfo());
    }

    public static class Info {

        @DrawableRes
        public final int iconRes;

        public final String infoString;

        public Info(@DrawableRes int iconRes, String infoString) {
            this.iconRes = iconRes;
            this.infoString = infoString;
        }

    }

}
