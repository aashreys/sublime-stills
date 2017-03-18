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
        Url imageUrl = image.getUrl(Image.UrlType.SHARE);
        if (imageUrl.value().length() > MAX_URL_LENGTH) {
            urlShortener.shorten(
                    imageUrl,
                    new UrlShortener.Listener() {
                        @Override
                        public void onComplete(@NonNull Url shortUrl) {
                            if (!isCancelled) {
                                startSharingIntent(context, shortUrl, image.getInfo().title);
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
            startSharingIntent(context, imageUrl, image.getInfo().title);
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
