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
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.aashreys.walls.R;
import com.aashreys.walls.utils.FileUtils;

import java.io.File;

/**
 * Created by aashreys on 05/04/17.
 */

public class SetAsAction {

    private static final String MIME_TYPE = "image/*";

    public void setAs(Context context, File file) throws ActivityNotFoundException {
        Uri uri = FileUtils.createUri(context, file);
        Intent intent = createSetAsIntent(context, uri);
        context.startActivity(intent);
    }

    private Intent createSetAsIntent(Context context, Uri uri) throws ActivityNotFoundException {
        Intent setWallpaperIntent = createSetWallpaperIntent(uri);
        Intent moreOptionsIntent = createMoreOptionsIntent(uri);

        PackageManager pm = context.getPackageManager();
        boolean isSetWallpaperSupported = isIntentSupported(pm, setWallpaperIntent);
        boolean isMoreOptionsSupported = isIntentSupported(pm, moreOptionsIntent);

        Intent setAsIntent = null;
        if (isSetWallpaperSupported && isMoreOptionsSupported) {
            setAsIntent = Intent.createChooser(
                    setWallpaperIntent, context.getResources()
                            .getString(R.string.share_set_as_title)
            );
            setAsIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    new LabeledIntent[]{
                            createLabeledIntent(
                                    context,
                                    moreOptionsIntent,
                                    R.string.share_more_options,
                                    R.drawable.ic_more_horiz_black_24dp
                            )
                    }
            );
        } else if (isSetWallpaperSupported) {
            setAsIntent = setWallpaperIntent;
        } else if (isMoreOptionsSupported) {
            setAsIntent = moreOptionsIntent;
        }
        if (setAsIntent != null) {
            return setAsIntent;
        } else {
            throw new ActivityNotFoundException();
        }
    }

    private LabeledIntent createLabeledIntent(
            Context context,
            Intent intent,
            @StringRes int label,
            @DrawableRes int icon
    ) {
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        intent.setComponent(componentName);
        return new LabeledIntent(intent, context.getPackageName(), label, icon);
    }

    private Intent createSetWallpaperIntent(Uri uri) {
        Intent setWallpaperIntent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
        setWallpaperIntent.setDataAndType(uri, MIME_TYPE);
        setWallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return setWallpaperIntent;
    }

    private Intent createMoreOptionsIntent(Uri uri) {
        Intent moreOptionsIntent = new Intent(Intent.ACTION_ATTACH_DATA);
        moreOptionsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        moreOptionsIntent.setDataAndType(uri, MIME_TYPE);
        moreOptionsIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return moreOptionsIntent;
    }

    private boolean isIntentSupported(PackageManager pm, Intent intent) {
        return intent.resolveActivity(pm) != null;
    }

}
