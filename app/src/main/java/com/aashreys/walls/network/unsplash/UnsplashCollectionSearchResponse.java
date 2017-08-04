package com.aashreys.walls.network.unsplash;

import com.aashreys.walls.DontObfuscate;

import java.util.List;

/**
 * Created by aashreys on 17/06/17.
 */

@DontObfuscate
public class UnsplashCollectionSearchResponse {

    private List<UnsplashCollectionResponse> results;

    public List<UnsplashCollectionResponse> getCollectionResponseList() {
        return results;
    }

}
