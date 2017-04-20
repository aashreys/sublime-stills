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

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

/**
 * Created by aashreys on 11/04/17.
 */

public class HintViewModel implements ViewModel {

    @Inject KeyValueStore keyValueStore;

    private String hint;

    private String seenKey;

    private EventListener eventListener;

    @Inject
    public HintViewModel() {}

    void setSeenKey(String key) {
        this.seenKey = key;
        notifySeenStatus();
    }

    private void notifySeenStatus() {
        if (seenKey != null && keyValueStore.getBoolean(seenKey, false)) {
            if (eventListener != null) {
                eventListener.onHintDismissed();
            }
        }
    }

    String getHint() {
        return hint;
    }

    void setHint(String hint) {
        this.hint = hint;
        if (eventListener != null) {
            eventListener.onHintSet();
        }
    }

    void onCloseButtonClicked() {
        if (seenKey != null) {
            keyValueStore.putBoolean(seenKey, true);
            if (eventListener != null) {
                eventListener.onHintDismissed();
            }
        }
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    interface EventListener {

        void onHintSet();

        void onHintDismissed();

    }

}
