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

import com.aashreys.walls.R;
import com.aashreys.walls.application.activities.StreamActivityModel;
import com.aashreys.walls.application.helpers.UiHelper;
import com.aashreys.walls.domain.device.ResourceProvider;
import com.aashreys.walls.domain.preferences.PreferenceService;
import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

/**
 * Created by aashreys on 01/09/17.
 */

public class SettingsFragmentModel {

    private final PreferenceService preferenceService;

    private final KeyValueStore keyValueStore;

    private final ResourceProvider resourceProvider;

    private EventListener eventListener;

    @Inject
    SettingsFragmentModel(
            PreferenceService preferenceService,
            KeyValueStore keyValueStore,
            ResourceProvider resourceProvider
    ) {
        this.preferenceService = preferenceService;
        this.keyValueStore = keyValueStore;
        this.resourceProvider = resourceProvider;
    }

    void configureDarkMode() {
        UiHelper.configureDayNightMode(
                preferenceService.isDarkModeEnabled(),
                preferenceService.isAutoDarkModeEnabled()
        );
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    void onShowTipsPrefClicked() {
        keyValueStore.putBoolean(resourceProvider.getString(R.string.tag_hint_collection), false);
        keyValueStore.putBoolean(
                resourceProvider.getString(R.string.tag_hint_image_actions),
                false
        );
        if (eventListener != null) {
            eventListener.onTipsReset();
        }
    }

    void onShowOnboardingPrefClicked() {
        keyValueStore.putBoolean(StreamActivityModel.KEY_IS_ONBOARDING_COMPLETED, false);
        if (eventListener != null) {
            eventListener.onOnboardingReset();
        }
    }

    interface EventListener {

        void onTipsReset();

        void onOnboardingReset();

    }

}
