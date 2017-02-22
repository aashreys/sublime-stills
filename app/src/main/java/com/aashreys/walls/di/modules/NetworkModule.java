package com.aashreys.walls.di.modules;

import com.aashreys.walls.network.UnsplashNetworkService;
import com.aashreys.walls.network.UnsplashNetworkServiceImpl;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.network.UrlShortenerImpl;
import com.aashreys.walls.network.UrlShortenerNetworkService;
import com.aashreys.walls.network.UrlShortenerNetworkServiceImpl;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class NetworkModule {

    public NetworkModule() {}

    @Provides
    public UrlShortener providesUrlShortener(
            UrlShortenerNetworkService networkService,
            ShortUrlRepository shortUrlRepository
    ) {
        return new UrlShortenerImpl(networkService, shortUrlRepository);
    }

    @Provides
    public UrlShortenerNetworkService providesUrlShortenerNetworkService(OkHttpClient httpClient) {
        return new UrlShortenerNetworkServiceImpl(httpClient);
    }

    @Provides
    public UnsplashNetworkService providesUnsplashNetworkService(OkHttpClient httpClient) {
        return new UnsplashNetworkServiceImpl(httpClient);
    }

    @Provides
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }

}
