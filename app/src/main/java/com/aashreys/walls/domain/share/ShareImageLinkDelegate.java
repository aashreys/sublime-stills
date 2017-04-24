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

package com.aashreys.walls.domain.share;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.actions.ShareImageLinkAction;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.application.helpers.UiHandler;
import com.aashreys.walls.utils.LogWrapper;

/**
 * Created by aashreys on 08/02/17.
 */

public class ShareImageLinkDelegate implements ShareDelegate {

    private static final String TAG = ShareImageLinkDelegate.class.getSimpleName();

    private static final int MAX_URL_LENGTH = 40;

    private final UrlShortener urlShortener;

    private final ShareImageLinkAction shareImageLinkAction;

    private final UiHandler uiHandler;

    private UrlShortenerListener urlShortenerListener;

    public ShareImageLinkDelegate(
            UrlShortener urlShortener,
            ShareImageLinkAction shareImageLinkAction,
            UiHandler uiHandler
    ) {
        this.urlShortener = urlShortener;
        this.shareImageLinkAction = shareImageLinkAction;
        this.uiHandler = uiHandler;
    }

    @Override
    public void share(final Context context, final Image image, final Listener listener) {
        urlShortenerListener = createUrlShortenerListener(context, image, listener);
        Url imageUrl = image.getShareUrl();
        if (imageUrl.value().length() > MAX_URL_LENGTH) {
            urlShortener.shorten(imageUrl, urlShortenerListener);
        } else {
            shareImageLinkAction.shareImageLink(context, image.getTitle(), imageUrl);
            listener.onShareComplete();
        }
    }

    public UrlShortenerListener createUrlShortenerListener(
            final Context context,
            final Image image,
            final Listener listener
    ) {
        return new UrlShortenerListener() {
            @Override
            public void onComplete(@NonNull Url shortUrl) {
                if (!isCancelled) {
                    shareImageLinkAction.shareImageLink(
                            context,
                            image.getTitle(),
                            shortUrl
                    );
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onShareComplete();
                        }
                    });
                }
            }

            @Override
            public void onError(@NonNull UrlShortener.UrlShortenerException e) {
                if (!isCancelled) {
                    LogWrapper.e(TAG, "Unable to shorten image url", e);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onShareFailed();
                        }
                    });
                }
            }
        };
    }

    @Override
    public void cancel() {
        if (urlShortenerListener != null) {
            urlShortenerListener.isCancelled = true;
        }
    }

    private abstract class UrlShortenerListener implements UrlShortener.Listener {

        boolean isCancelled;

    }
}
