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

import android.support.annotation.DrawableRes;
import android.view.Gravity;

import com.aashreys.maestro.ViewModel;

/**
 * Created by aashreys on 11/04/17.
 */

public class InfoViewModel implements ViewModel {

    private EventListener eventListener;

    void setTitle(String title) {
        if (eventListener != null) {
            eventListener.onTitleSet(title);
        }
    }

    void setTitleIcon(int titleIcon) {
        if (eventListener != null) {
            eventListener.onTitleIconSet(titleIcon);
        }
    }

    void setInfo(String info) {
        if (eventListener != null) {
            eventListener.onInfoSet(info);
        }
    }

    int getGravity() {
        return Gravity.CENTER_VERTICAL;
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    interface EventListener {

        void onTitleSet(String title);

        void onTitleIconSet(@DrawableRes int icon);

        void onInfoSet(String info);

    }

}
