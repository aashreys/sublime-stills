package com.aashreys.walls.domain.share;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
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

    private final UrlShortener urlShortener;

    private boolean isCancelled;

    private UiHandler uiHandler = new UiHandler();

    ImageUrlSharer(@Provided UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    @Override
    public void share(final Context context, Image image, final Listener listener) {
        Url imageUrl = image.fullImageUrl();
        Url urlToShare = urlShortener.shortenLocal(imageUrl);
        if (urlToShare != null) {
            startSharingIntent(context, urlToShare);
            listener.onShareComplete();
        } else {
            urlShortener.shortenAsync(
                    imageUrl,
                    new UrlShortener.Listener() {
                        @Override
                        public void onComplete(@NonNull Url shortUrl) {
                            if (!isCancelled) {
                                startSharingIntent(context, shortUrl);
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
        }
    }

    @Override
    public void cancel() {
        boolean isCancelled = true;
    }

    private void startSharingIntent(Context context, Url urlToShare) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.share_text_template, urlToShare.value())
        );
        shareIntent.setType(MIME_TYPE);
        context.startActivity(Intent.createChooser(
                shareIntent,
                context.getResources().getText(R.string.share_title)
        ));
    }
}
