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
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aashreys.walls.R;

/**
 * Created by aashreys on 10/03/17.
 */

public class LoadingView extends LinearLayout {

    private ProgressBar progressBar;

    private ImageView infoIcon;

    private TextView infoText;

    private Button actionButton;

    public LoadingView(Context context) {
        super(context);
        _init(context);
    }

    public LoadingView(
            Context context,
            @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        _init(context);
    }

    public LoadingView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context);
    }

    private void _init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_view_loading, this, true);

        progressBar = (ProgressBar) findViewById(R.id.progress_loading);
        infoIcon = (ImageView) findViewById(R.id.image_loading);
        infoText = (TextView) findViewById(R.id.text_loading);
        actionButton = (Button) findViewById(R.id.button_action);
    }

    public void setText(@StringRes int text) {
        infoText.setText(text);
    }

    public void hideText() {
        infoText.setVisibility(GONE);
    }

    public void showText() {
        infoText.setVisibility(VISIBLE);
    }

    public void setActionButtonText(@StringRes int text) {
        actionButton.setText(text);
    }

    public void setActionButtonOnClickListener(OnClickListener onClickListener) {
        actionButton.setOnClickListener(onClickListener);
    }

    public void setIcon(@DrawableRes int icon) {
        infoIcon.setImageResource(icon);
    }

    public void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    public void showIcon() {
        infoIcon.setVisibility(VISIBLE);
    }

    public void hideIcon() {
        infoIcon.setVisibility(GONE);
    }

    public void hideActionButton() {
        actionButton.setVisibility(GONE);
    }

    public void showActionButton() {
        actionButton.setVisibility(VISIBLE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

}
