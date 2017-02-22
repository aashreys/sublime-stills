package com.aashreys.walls.domain.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.aashreys.walls.R;
import com.aashreys.walls.Utils;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.ui.utils.UiHandler;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by aashreys on 05/12/16.
 */

class SetAsSharer implements Sharer {

    private static final String TAG = SetAsSharer.class.getSimpleName();

    private static final String MIME_TYPE = "image/*";

    private boolean isCancelled;

    private UiHandler uiHandler = new UiHandler();

    @Override
    public void share(final Context context, final Image image, final Listener listener) {
        final Url imageUrl = image.regularImageUrl();
        Utils.downloadImageAsync(
                context,
                imageUrl,
                new SimpleTarget<File>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onShareFailed();
                            }
                        });
                    }

                    @Override
                    public void onResourceReady(
                            File resource, GlideAnimation<? super File> glideAnimation
                    ) {
                        if (!isCancelled) {
                            // Glide uses an external cache so our files are shareable by default
                            Uri imageUri = Uri.fromFile(resource);
                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setDataAndType(imageUri, MIME_TYPE);
                            intent.putExtra("mimeType", MIME_TYPE);
                            context.startActivity(Intent.createChooser(
                                    intent,
                                    context.getString(R.string.share_set_as_title)
                            ));
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onShareComplete();
                                }
                            });
                        }
                    }
                }
        );
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
