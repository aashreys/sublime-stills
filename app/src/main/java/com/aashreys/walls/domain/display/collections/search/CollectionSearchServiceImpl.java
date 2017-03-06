package com.aashreys.walls.domain.display.collections.search;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.collections.Collection;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 05/03/17.
 */

public class CollectionSearchServiceImpl implements CollectionSearchService {

    private final UnsplashCollectionSearchService unsplashCollectionSearchService;

    private final FlickrTagSearchService flickrTagSearchService;

    @Inject
    public CollectionSearchServiceImpl(
            UnsplashCollectionSearchService unsplashCollectionSearchService,
            FlickrTagSearchService flickrTagSearchService
    ) {
        this.unsplashCollectionSearchService = unsplashCollectionSearchService;
        this.flickrTagSearchService = flickrTagSearchService;
    }

    @NonNull
    @Override
    public List<Collection> search(String collection) {
        return flickrTagSearchService.search(collection);
    }
}
