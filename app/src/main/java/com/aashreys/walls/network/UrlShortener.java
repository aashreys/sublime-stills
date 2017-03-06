package com.aashreys.walls.network;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 03/12/16.
 */

public interface UrlShortener {

    void shorten(Url longUrl, Listener listener);

    interface Listener {

        void onComplete(@NonNull Url shortUrl);

        void onError(@NonNull UrlShortenerException e);

    }

    class UrlShortenerException extends Exception {

        public UrlShortenerException() {}

        protected UrlShortenerException(String message) {
            super(message);
        }

        protected UrlShortenerException(String message, Throwable cause) {
            super(message, cause);
        }

        protected UrlShortenerException(Throwable cause) {
            super(cause);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected UrlShortenerException(
                String message,
                Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace
        ) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
