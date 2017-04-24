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

package com.aashreys.walls.application.helpers;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

import com.aashreys.walls.application.WallsApplication;
import com.aashreys.walls.di.UiComponent;

/**
 * Created by aashreys on 03/03/17.
 */

public class UiHelper {

    public static int getPageNumber(int fromIndex, int itemsPerPage) {
        return (fromIndex / itemsPerPage) + 1;
    }

    public static int getColor(Context context, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(color, null);
        } else {
            return context.getResources().getColor(color);
        }
    }

    public static UiComponent getUiComponent(Context context) {
       return ((WallsApplication) context.getApplicationContext()).getApplicationComponent().getUiComponent();
    }
}
