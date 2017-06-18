package com.aashreys.walls.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by aashreys on 18/06/17.
 */

public class FileUtils {

    public static Uri createUri(Context context, File file) {
        return FileProvider.getUriForFile(
                context,
                context.getApplicationContext().getPackageName() + ".provider",
                file
        );
    }

}
