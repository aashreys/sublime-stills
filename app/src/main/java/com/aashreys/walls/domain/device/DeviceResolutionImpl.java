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

package com.aashreys.walls.domain.device;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by aashreys on 19/03/17.
 */

public class DeviceResolutionImpl implements DeviceResolution {

    private final int width, height;

    public DeviceResolutionImpl(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        boolean isPortrait = context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            this.width = dm.widthPixels;
            this.height = dm.heightPixels;
        } else {
            this.width = dm.heightPixels;
            this.height = dm.widthPixels;
        }
    }


    public int getPortraitWidth() {
        return width;
    }

    public int getPortraitHeight() {
        return height;
    }
}

