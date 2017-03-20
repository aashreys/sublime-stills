package com.aashreys.walls.ui.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;

import com.aashreys.walls.R;

import java.util.List;

/**
 * Created by aashreys on 03/03/17.
 */

public class ChromeTabHelper {

    /**
     * Helper to open urls in a browser
     */
    public static void openUrl(Context context, String url) {
        if (isChromeTabSupported(context)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(UiHelper.getColor(context, R.color.appBarBackground));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    private static boolean isChromeTabSupported(Context context) {
        // Get default VIEW intent handler that can view a web url.
        Intent activityIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.test-url.com")
        );

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
}
