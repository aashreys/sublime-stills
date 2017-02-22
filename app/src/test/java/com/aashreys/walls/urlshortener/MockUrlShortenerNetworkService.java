package com.aashreys.walls.urlshortener;

import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerNetworkService;

/**
 * Created by aashreys on 18/02/17.
 */

public class MockUrlShortenerNetworkService implements UrlShortenerNetworkService {

    public static final int
            MODE_ALWAYS_SUCCESS = 0,
            MODE_ALWAYS_FAILURE = 1;

    private int mode;

    private Url shortUrl;

    public MockUrlShortenerNetworkService() {
        this.shortUrl = new Url("https://abc.com");
    }

    @Override
    public void postAsync(
            String url, String apiKey, String postData, UrlShortener.Listener listener
    ) {
        switch (mode) {

            case MODE_ALWAYS_SUCCESS:
                listener.onComplete(shortUrl);
                break;

            case MODE_ALWAYS_FAILURE:
                listener.onError(new UrlShortener.UrlShortenerException());

        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = new Url(shortUrl);
    }
}
