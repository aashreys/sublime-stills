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

import com.aashreys.walls.R;

/**
 * Created by aashreys on 09/04/17.
 */

public class DeviceInfoImpl implements DeviceInfo {

    private final Context context;

    private final DeviceResolution deviceResolution;

    public DeviceInfoImpl(Context context) {
        this.context = context;
        this.deviceResolution = new DeviceResolutionImpl(context);
    }

    @Override
    public DeviceResolution getDeviceResolution() {
        return deviceResolution;
    }

    @Override
    public Orientation getOrientation() {
        int orientation = context.getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            default:
                return Orientation.OTHER;
        }
    }

    @Override
    public int getNumberOfStreamColumns() {
        return context.getResources().getInteger(R.integer.stream_column_count);
    }
}
