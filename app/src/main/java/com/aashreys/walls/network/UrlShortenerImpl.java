package com.aashreys.walls.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.safeapi.SafeApi;
import com.aashreys.walls.BuildConfig;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;

/**
 * Created by aashreys on 03/12/16.
 */

public class UrlShortenerImpl implements UrlShortener {

    private static final String BASE_URL = "https://www.googleapis.com/urlshortener/v1/url?key=%s";

    private static final String POST_STRING_TEMPLATE = "{\"longUrl\": \"%s\"}";

    private final UrlShortenerNetworkService networkService;

    private final ShortUrlRepository shortUrlRepo;

    public UrlShortenerImpl(
            UrlShortenerNetworkService networkService,
            ShortUrlRepository shortUrlRepo
    ) {
        this.networkService = networkService;
        this.shortUrlRepo = shortUrlRepo;
    }

    @Override
    public void shortenAsync(final Url longUrl, final Listener listener) {
        if (longUrl != null && longUrl.isValid()) {
            networkService.postAsync(
                    BASE_URL,
                    SafeApi.decrypt(BuildConfig.GOOGL_API_KEY),
                    getPostString(longUrl),
                    new Listener() {
                        @Override
                        public void onComplete(@NonNull Url shortUrl) {
                            shortUrlRepo.save(longUrl, shortUrl);
                            listener.onComplete(shortUrl);
                        }

                        @Override
                        public void onError(@NonNull UrlShortenerException e) {
                            listener.onError(e);
                        }
                    }
            );
        } else {
            listener.onError(new UrlShortenerException("Url is invalid"));
        }
    }

    @Nullable
    @Override
    public Url shortenLocal(Url longUrl) {
        return shortUrlRepo.get(longUrl);
    }


    private String getPostString(Url url) {
        return String.format(POST_STRING_TEMPLATE, url.value());
    }
}
