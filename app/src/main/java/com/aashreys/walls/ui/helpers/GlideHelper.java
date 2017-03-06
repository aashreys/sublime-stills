package com.aashreys.walls.ui.helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aashreys.walls.domain.values.Url;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by aashreys on 03/03/17.
 */

public class GlideHelper {

    /**
     * Helper to display images asynchronously
     */
    public static void displayImageAsync(
            Fragment fragment,
            Url imageUrl,
            ImageView target,
            Priority priority
    ) {
        Glide.with(fragment)
                .load(imageUrl.value())
                .priority(priority)
                .crossFade()
                .into(target);
    }

    public static void downloadImageAsync(Context context, Url imageUrl, Target<File> target) {
        Glide.with(context)
                .load(imageUrl.value())
                .downloadOnly(target);
    }
}
