package com.aashreys.walls.network;

import android.support.annotation.Nullable;

/**
 * Created by aashreys on 09/02/17.
 */

public interface UnsplashNetworkService {

    @Nullable
    String get(String url, String appId, String apiVersion);

}
