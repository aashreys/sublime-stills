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

package com.aashreys.walls.urlshortener;

import android.support.annotation.NonNull;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerImpl;
import com.aashreys.walls.network.apis.ApiInstanceCreator;
import com.aashreys.walls.network.apis.UrlShortenerApi;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by aashreys on 18/02/17.
 */

public class UrlShortenerTests extends BaseTestCase {

    private static final int WAIT_TIME = 200;

    private static final String MOCK_RESPONSE = "{\n" +
            "  \"kind\": \"urlshortener#url\",\n" +
            "  \"id\": \"https://apple.com\",\n" +
            "  \"longUrl\": \"https://google.com\"\n" +
            "}";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private MockWebServer mockWebServer;

    private UrlShortenerImpl urlShortener;

    private UrlShortenerApi urlShortenerApi;

    private MockShortUrlRepository shortUrlRepository;

    private CountDownLatch lock = new CountDownLatch(1);

    private boolean isError;

    @Override
    public void init() {
        super.init();
        mockWebServer = new MockWebServer();
        HttpUrl baseUrl = mockWebServer.url("");
        shortUrlRepository = new MockShortUrlRepository();
        shortUrlRepository = Mockito.spy(shortUrlRepository);
        urlShortenerApi = new ApiInstanceCreator().createUrlShortenerApi(
                new OkHttpClient(),
                baseUrl.toString()
        );
        urlShortener = new UrlShortenerImpl(shortUrlRepository, urlShortenerApi);
    }

    @Test
    public void test_null_args() throws InterruptedException {
        urlShortener.shorten(null, new UrlShortener.Listener() {
            @Override
            public void onComplete(@NonNull Url shortUrl) {
                fail();
            }

            @Override
            public void onError(@NonNull UrlShortener.UrlShortenerException e) {
                isError = true;
            }
        });
        lock.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        assertTrue(isError);
    }

    @Test
    public void test_invalid_args() throws InterruptedException {
        urlShortener.shorten(
                new Url("bugs bunny does not like the internet"),
                new UrlShortener.Listener() {
                    @Override
                    public void onComplete(@NonNull Url shortUrl) {
                        fail();
                    }

                    @Override
                    public void onError(@NonNull UrlShortener.UrlShortenerException e) {
                        isError = true;
                    }
                }
        );
        lock.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        assertTrue(isError);
    }

    @Test
    public void test_url_caching() throws InterruptedException {
        // Long url must be greater than 40 characters to be shortened.
        final Url longUrl = new Url("https://google.com");
        final String shortUrlString = "https://apple.com";
        mockWebServer.enqueue(new MockResponse().setBody(MOCK_RESPONSE));
        urlShortener.shorten(longUrl, new UrlShortener.Listener() {
            @Override
            public void onComplete(@NonNull Url shortUrl) {
                assertEquals(shortUrl.value(), shortUrlString);

                // Verify short url was cached
                Mockito.verify(shortUrlRepository, Mockito.times(1)).save(longUrl, shortUrl);
                assertEquals(shortUrlRepository.get(longUrl).value(), shortUrlString);
            }

            @Override
            public void onError(@NonNull UrlShortener.UrlShortenerException e) {
                fail();
            }
        });
        lock.await(WAIT_TIME, TimeUnit.MILLISECONDS);

        // Verify second call gets cached data instead of a network call
        urlShortener.shorten(
                longUrl,
                new UrlShortener.Listener() {
                    @Override
                    public void onComplete(@NonNull Url shortUrl) {
                        assertEquals(shortUrl.value(), shortUrlString);
                        Mockito.verify(shortUrlRepository, Mockito.times(3)).get(longUrl);
                    }

                    @Override
                    public void onError(@NonNull UrlShortener.UrlShortenerException e) {

                    }
                }
        );
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

}
