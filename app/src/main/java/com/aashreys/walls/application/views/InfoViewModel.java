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

import android.support.annotation.DrawableRes;
import android.view.Gravity;

import com.aashreys.maestro.ViewModel;

import javax.inject.Inject;

import static com.aashreys.walls.application.views.InfoView.Info;

/**
 * Created by aashreys on 11/04/17.
 */

class InfoViewModel implements ViewModel {

    private EventListener eventListener;

    private Info info;

    @Inject
    public InfoViewModel() {}

    @DrawableRes
    int getIcon() {
        return info.iconRes;
    }

    String getInfo() {
        return info.infoString;
    }

    void setInfo(Info info) {
        this.info = info;
        if (eventListener != null) {
            eventListener.onInfoSet();
        }
    }

    int getGravity() {
        return Gravity.CENTER_VERTICAL;
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    interface EventListener {

        void onInfoSet();

    }

}
