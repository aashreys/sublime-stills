package com.aashreys.walls.domain.display.sources;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.Config;
import com.aashreys.walls.Utils;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.ServerId;
import com.aashreys.walls.network.UnsplashNetworkService;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

/**
 * Created by aashreys on 31/01/17.
 */

@AutoFactory
public class UnsplashCollectionSource implements Source {

    private static final String TAG = UnsplashCollectionSource.class.getSimpleName();

    private static final String BASE_URL
            = "https://api.unsplash.com//collections/%s/photos?page=%d&per_page=%d";

    private final UnsplashNetworkService networkService;

    private final UnsplashImageResponseParser responseParser;

    private final ServerId collectionId;

    public UnsplashCollectionSource(
            @Provided UnsplashNetworkService networkService,
            @Provided UnsplashImageResponseParser responseParser,
            ServerId collectionId
    ) {
        this.networkService = networkService;
        this.responseParser = responseParser;
        this.collectionId = collectionId;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public List<Image> getImages(int fromIndex) {
        String url = String.format(
                BASE_URL,
                collectionId.value(),
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
