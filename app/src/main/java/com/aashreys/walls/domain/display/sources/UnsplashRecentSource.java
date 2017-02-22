package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.Config;
import com.aashreys.walls.Utils;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.network.UnsplashNetworkService;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

/**
 * Created by aashreys on 21/11/16.
 */

@AutoFactory
public class UnsplashRecentSource implements Source {

    private static final String BASE_URL = "https://api.unsplash.com/photos?page=%d&per_page=%d";

    private UnsplashNetworkService networkService;

    private final UnsplashImageResponseParser responseParser;

    public UnsplashRecentSource(
            @Provided UnsplashNetworkService networkService,
            @Provided UnsplashImageResponseParser responseParser
    ) {
        this.networkService = networkService;
        this.responseParser = responseParser;
    }

    @NonNull
    @WorkerThread
    @Override
    public List<Image> getImages(int fromIndex) {
        String url = String.format(
                BASE_URL,
                Utils.getPageNumber(fromIndex, Config.Unsplash.ITEMS_PER_PAGE),
                Config.Unsplash.ITEMS_PER_PAGE
        );
        String response = networkService.get(
                url,
                SafeApi.decrypt(BuildConfig.UNSPLASH_API_KEY),
                Config.Unsplash.API_VERSION
        );
        return responseParser.parse(response);
    }

}
