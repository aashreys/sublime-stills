package com.aashreys.walls.urlshortener;

import android.support.annotation.NonNull;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerImpl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by aashreys on 18/02/17.
 */

public class UrlShortenerTests extends BaseTestCase {

    private static final int WAIT_TIME = 200;

    private UrlShortenerImpl urlShortener;

    private MockUrlShortenerNetworkService networkService;

    private MockShortUrlRepository shortUrlRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private CountDownLatch lock = new CountDownLatch(1);

    private boolean isError;

    @Override
    public void init() {
        super.init();
        shortUrlRepository = new MockShortUrlRepository();
        networkService = new MockUrlShortenerNetworkService();
        urlShortener = new UrlShortenerImpl(networkService, shortUrlRepository);
    }

    @Test
    public void test_invalid_args() throws InterruptedException {
        urlShortener.shortenAsync(null, new UrlShortener.Listener() {
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
    public void test_null_args() throws InterruptedException {
        urlShortener.shortenAsync(new Url("bugs bunny does not like the internet"), new UrlShortener.Listener() {
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
    public void test_valid_args_and_url_caching() {
        final Url longUrl = new Url("https://apple.com");
        final String shortUrlString = "https://google.com";
        networkService.setMode(MockUrlShortenerNetworkService.MODE_ALWAYS_SUCCESS);
        networkService.setShortUrl(shortUrlString);
        urlShortener.shortenAsync(longUrl, new UrlShortener.Listener() {
            @Override
            public void onComplete(@NonNull Url shortUrl) {
                assertEquals(shortUrl.value(), shortUrlString);
                assertEquals(shortUrlRepository.get(longUrl).value(), shortUrlString);
                assertEquals(urlShortener.shortenLocal(longUrl).value(), shortUrlString);
            }

            @Override
            public void onError(@NonNull UrlShortener.UrlShortenerException e) {
                fail();
            }
        });
    }

}
