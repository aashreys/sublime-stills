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

package com.aashreys.walls.ui;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.ResourceProvider;
import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/04/17.
 */

public class SettingsActivityModel implements ViewModel {

    @Inject KeyValueStore keyValueStore;

    @Inject ResourceProvider resourceProvider;

    private EventListener eventListener;

    void onResetTipsSettingClicked() {
        keyValueStore.putBoolean(resourceProvider.getString(R.string.tag_hint_collection), false);
        keyValueStore.putBoolean(
                resourceProvider.getString(R.string.tag_hint_image_actions),
                false
        );
        if (eventListener != null) {
            eventListener.onTipsReset();
        }
    }

    void onResetOnboardingSettingClicked() {
        keyValueStore.putBoolean(StreamActivityModel.KEY_IS_ONBOARDING_COMPLETED, false);
        if (eventListener != null) {
            eventListener.onOnboardingReset();
        }
    }

    void onInjectionComplete() {

    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    interface EventListener {

        void onTipsReset();

        void onOnboardingReset();

    }

}
