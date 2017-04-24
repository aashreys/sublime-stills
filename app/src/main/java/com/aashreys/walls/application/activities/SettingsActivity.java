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

package com.aashreys.walls.application.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aashreys.walls.R;

public class SettingsActivity extends BaseActivity<SettingsActivityModel> implements
        SettingsActivityModel.EventListener {

    private TextView resetTipsText;

    private TextView resetOnboardingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getViewModel().setEventListener(this);

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetTipsText = (TextView) findViewById(R.id.text_display_tips);
        resetTipsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().onResetTipsSettingClicked();
            }
        });

        resetOnboardingText = (TextView) findViewById(R.id.text_display_onboarding);
        resetOnboardingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().onResetOnboardingSettingClicked();
            }
        });
    }

    @Override
    protected SettingsActivityModel createViewModel() {
        SettingsActivityModel viewModel = getUiComponent().createSettingsActivityModel();
        viewModel.onInjectionComplete();
        return viewModel;
    }

    @Override
    public void onTipsReset() {
        Snackbar.make(
                resetTipsText,
                R.string.confirmation_show_tips_again,
                Snackbar.LENGTH_LONG
        ).show();
    }

    @Override
    public void onOnboardingReset() {
        Snackbar.make(
                resetTipsText,
                R.string.confirmation_show_onboarding_again,
                Snackbar.LENGTH_LONG
        ).show();
    }
}
