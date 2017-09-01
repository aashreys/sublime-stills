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

package com.aashreys.walls.domain.preferences;

import android.content.Context;
import android.preference.PreferenceManager;

import com.aashreys.walls.R;

/**
 * Created by aashreys on 01/09/17.
 */

public class PreferenceServiceImpl implements PreferenceService {

    private final Context context;

    public PreferenceServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean isDarkModeEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_use_dark_mode), false);
    }

    @Override
    public boolean isAutoDarkModeEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_auto_dark_mode), false);
    }
}
