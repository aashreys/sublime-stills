package com.aashreys.walls.network;

/**
 * Created by aashreys on 10/02/17.
 */

public interface UrlShortenerNetworkService {

    void postAsync(
            String url,
            String apiKey,
            String postData,
            UrlShortener.Listener listener
    );


}
