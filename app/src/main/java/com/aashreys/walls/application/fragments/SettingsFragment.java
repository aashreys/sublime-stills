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

package com.aashreys.walls.application.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.aashreys.walls.R;
import com.aashreys.walls.application.helpers.UiHelper;

/**
 * Created by aashreys on 01/09/17.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences
        .OnSharedPreferenceChangeListener, SettingsFragmentModel.EventListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private SettingsFragmentModel viewModel;

    private String prefDarkModeKey, prefAutoDarkModeKey, prefShowOnboardingKey;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = UiHelper.getUiComponent(getActivity()).createSettingsFragmentModel();
        viewModel.setEventListener(this);


        setupPreferenceKeys();
        setupPreferences();
    }

    private void setupPreferenceKeys() {
        prefDarkModeKey = getActivity().getString(R.string.pref_use_dark_mode);
        prefAutoDarkModeKey = getActivity().getString(R.string.pref_auto_dark_mode);
        prefShowOnboardingKey = getActivity().getString(R.string.pref_show_onboarding);
    }

    private void setupPreferences() {
        addPreferencesFromResource(R.xml.preferences);
        findPreference(prefShowOnboardingKey).setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                viewModel.onShowOnboardingPrefClicked();
                return true;
            }
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefDarkModeKey) || key.equals(prefAutoDarkModeKey)) {
            viewModel.configureDarkMode();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onTipsReset() {
        showSnackbarMessage(R.string.confirmation_show_tips_again, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onOnboardingReset() {
        showSnackbarMessage(R.string.confirmation_show_onboarding_again, Snackbar.LENGTH_LONG);
    }

    private void showSnackbarMessage(@StringRes int message, int length) {
        View parentView = getView();
        if (parentView != null) {
            Snackbar.make(getView(), message, length).show();
        }
    }

}
