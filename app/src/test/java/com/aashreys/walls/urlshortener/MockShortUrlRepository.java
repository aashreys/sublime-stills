package com.aashreys.walls.urlshortener;

import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aashreys on 18/02/17.
 */

public class MockShortUrlRepository implements ShortUrlRepository {

    private Map<Url, Url> urlMap = new HashMap<>();

    @Nullable
    @Override
    public Url get(@Nullable Url longUrl) {
        return urlMap.get(longUrl);
    }

    @Override
    public void save(Url longUrl, Url shortUrl) {
        urlMap.put(longUrl, shortUrl);
    }
}
