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

