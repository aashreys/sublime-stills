package com.aashreys.walls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashRecentCollection;
import com.aashreys.walls.domain.values.Url;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.List;

/**
 * Created by aashreys on 23/11/16.
 */

public class Utils {

    /**
     * Helper to get column count for image stream
     */
    public static int getStreamColumnCount(Context context) {
        return context.getResources().getInteger(R.integer.stream_column_count);
    }

    /**
     * Helper to display images asynchronously
     */
    public static void displayImageAsync(
            Fragment fragment,
            Url imageUrl,
            ImageView target,
            Priority priority
    ) {
        configureGlideRequestManager(Glide.with(fragment), imageUrl, target, priority);
    }

    /**
     * Helper to display images asynchronously
     */
    public static void displayImageAsync(
            Activity activity,
            Url imageUrl,
            ImageView target,
            Priority priority
    ) {
        configureGlideRequestManager(Glide.with(activity), imageUrl, target, priority);
    }

    private static void configureGlideRequestManager(RequestManager requestManager, Url imageUrl, ImageView target, Priority priority) {
        requestManager.load(imageUrl.value())
                .priority(priority)
                .placeholder(new ColorDrawable()) // To add empty space
                .crossFade()
                .into(target);
    }

    public static void downloadImageAsync(Context context, Url imageUrl, Target<File> target) {
        Glide.with(context)
                .load(imageUrl.value())
                .downloadOnly(target);
    }

    /**
     * Helper to open urls in a browser
     */
    public static void openUrl(Context context, String url) {
        if (isChromeTabSupported(context)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getColor(context, R.color.toolbarBackground));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    private static boolean isChromeTabSupported(Context context) {
        // Get default VIEW intent handler that can view a web url.
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.test-url.com"));

        // Get all apps that can handle VIEW intents.
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                return true;
            }
        }
        return false;
    }

    public static int getPageNumber(int fromIndex, int itemsPerPage) {
        return (fromIndex / itemsPerPage) + 1;
    }

    @StringRes
    public static int getTitleForCollectionType(@Collection.Type String type) {
        switch (type) {
            case Collection.Type.UNSPLASH_COLLECTION:
                return R.string.title_unsplash_collection;

            default:
                return 0;
        }
    }

    @DrawableRes
    public static int getIconForCollectionType(@Collection.Type String type) {
        switch (type) {
            case Collection.Type.UNSPLASH_RECENT:
            case Collection.Type.UNSPLASH_COLLECTION:
                return R.drawable.ic_unsplash_logo_black_24dp;

            case Collection.Type.FAVORITE:
                return R.drawable.ic_favorite_black_24dp;

            default:
                return 0;
        }
    }

    @Collection.Type
    public static String getCollectionType(Collection collection) {
        if (collection instanceof UnsplashCollection) {
            return Collection.Type.UNSPLASH_COLLECTION;
        }
        if (collection instanceof UnsplashRecentCollection) {
            return Collection.Type.UNSPLASH_RECENT;
        }
        if (collection instanceof FavoriteCollection) {
            return Collection.Type.FAVORITE;
        }
        return null;
    }

    public static int getColor(Context context, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(color, null);
        } else {
            return context.getResources().getColor(color);
        }
    }

}
