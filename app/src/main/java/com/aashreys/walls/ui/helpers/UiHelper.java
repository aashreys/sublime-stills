package com.aashreys.walls.ui.helpers;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

import com.aashreys.walls.R;

/**
 * Created by aashreys on 03/03/17.
 */

public class UiHelper {

    /**
     * Helper to get column count for image stream
     */
    public static int getStreamColumnCount(Context context) {
        return context.getResources().getInteger(R.integer.stream_column_count);
    }

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
}
