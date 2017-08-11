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

package com.aashreys.walls.domain.share.actions;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.File;

/**
 * Created by aashreys on 11/08/17.
 */

public class SetWallpaperAction {

    public void setWallpaper(Context context, File file) throws Exception {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (isSetWallpaperSupported(wallpaperManager)) {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            throw new UnsupportedOperationException("Not allowed to set wallpaper");
        }
    }

    private boolean isSetWallpaperSupported(WallpaperManager wallpaperManager) {
        boolean isSetWallpaperSupported;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isSetWallpaperSupported = wallpaperManager.isSetWallpaperAllowed();
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            isSetWallpaperSupported = wallpaperManager.isWallpaperSupported();
        } else {
            isSetWallpaperSupported = true;
        }
        return isSetWallpaperSupported;
    }

}
