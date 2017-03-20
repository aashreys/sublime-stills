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
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;
import com.aashreys.walls.ui.utils.UiHandler;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

/**
 * Created by aashreys on 08/02/17.
 */

@AutoFactory
public class ImageUrlSharer implements Sharer {

    private static final String TAG = ImageUrlSharer.class.getSimpleName();

    private static final String MIME_TYPE = "text/plain";

    private static final int MAX_URL_LENGTH = 40;

    private final UrlShortener urlShortener;

    private boolean isCancelled;

    private UiHandler uiHandler = new UiHandler();

    ImageUrlSharer(@Provided UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    @Override
    public void share(final Context context, final Image image, final Listener listener) {
        Url imageUrl = image.getShareUrl();
        if (imageUrl.value().length() > MAX_URL_LENGTH) {
            urlShortener.shorten(
                    imageUrl,
                    new UrlShortener.Listener() {
                        @Override
                        public void onComplete(@NonNull Url shortUrl) {
                            if (!isCancelled) {
                                startSharingIntent(context, shortUrl, image.getTitle());
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
                                Log.e(TAG, "Unable to shorten image url", e);
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onShareFailed();
                                    }
                                });
                            }
                        }
                    }
            );
        } else {
            startSharingIntent(context, imageUrl, image.getTitle());
            listener.onShareComplete();
        }
    }

    @Override
    public void cancel() {
        this.isCancelled = true;
    }

    private void startSharingIntent(Context context, Url imageUrl, Name title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String contentString;
        if (title != null && title.isValid()) {
            contentString = context.getResources().getString(
                    R.string.share_text_template_with_title,
                    title.value(),
                    imageUrl.value()
            );
        } else {
            contentString = context.getResources().getString(
                    R.string.share_text_template,
                    imageUrl.value()
            );
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, contentString);
        shareIntent.setType(MIME_TYPE);
        context.startActivity(Intent.createChooser(
                shareIntent,
                context.getResources().getText(R.string.share_title)
        ));
    }
}
