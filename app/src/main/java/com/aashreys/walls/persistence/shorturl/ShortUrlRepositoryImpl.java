package com.aashreys.walls.persistence.shorturl;


import com.aashreys.walls.domain.values.Url;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * Created by aashreys on 04/12/16.
 */

public class ShortUrlRepositoryImpl implements ShortUrlRepository {

    private static final String BOOK_NAME = "short_url_book";

    public ShortUrlRepositoryImpl() {}

    @Override
    public Url get(Url longUrl) {
        String shortUrlString = getBook().read(longUrl.value(), null);
        if (shortUrlString != null) {
            return new Url(shortUrlString);
        }
        return null;
    }

    @Override
    public void save(Url longUrl, Url shortUrl) {
        getBook().write(longUrl.value(), shortUrl.value());
    }

    private Book getBook() {
        return Paper.book(BOOK_NAME);
    }
}
