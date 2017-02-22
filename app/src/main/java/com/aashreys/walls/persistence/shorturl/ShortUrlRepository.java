package com.aashreys.walls.persistence.shorturl;

import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 04/12/16.
 */

public interface ShortUrlRepository {

    @Nullable
    Url get(@Nullable Url longUrl);

    void save(Url longUrl, Url shortUrl);

}
