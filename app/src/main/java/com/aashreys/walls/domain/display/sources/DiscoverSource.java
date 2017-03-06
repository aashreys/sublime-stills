package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;

import java.util.List;

/**
 * Created by aashreys on 03/03/17.
 */

public class DiscoverSource implements Source {

    private final FlickrRecentSource flickRecentSource;

    private final UnsplashRecentSource unsplashRecentSource;

    public DiscoverSource(FlickrRecentSource source, UnsplashRecentSource unsplashRecentSource) {
        this.flickRecentSource = source;
        this.unsplashRecentSource = unsplashRecentSource;
    }

    @NonNull
    @Override
    public List<Image> getImages(int fromIndex) {
        // TODO: Integrate Unsplash
        return flickRecentSource.getImages(fromIndex);
    }
}
