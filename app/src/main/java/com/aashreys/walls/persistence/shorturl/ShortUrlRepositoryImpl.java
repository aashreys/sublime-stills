/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
