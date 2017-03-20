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
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.ui.helpers.GlideHelper;
import com.aashreys.walls.ui.utils.UiHandler;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by aashreys on 20/03/17.
 */

public class ImageFileShareDelegate implements ShareDelegate {

    private static final int minWidth = 720;

    private static final String MIME_IMAGE = "image/*";

    private final DeviceResolution deviceResolution;

    private final UiHandler uiHandler = new UiHandler();

    private boolean isCancelled;

    ImageFileShareDelegate(DeviceResolution deviceResolution) {
        this.deviceResolution = deviceResolution;
    }

    @Override
    public void share(
            final Context context, final Image image, final Listener listener
    ) {
        int width = deviceResolution.getPortraitWidth() > minWidth ?
                deviceResolution.getPortraitWidth() :
                minWidth;
        final Url imageUrl = image.getUrl(width);
        GlideHelper.downloadImageAsync(
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
                            // Glide has been configured to use an external cache so that cached
                            // images are shareable by default. See {@link @GlideConfiguration}.
                            Uri imageUri = Uri.fromFile(resource);
                            startSharingIntent(context, imageUri, image);
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

    private void startSharingIntent(Context context, Uri imageUri, Image image) {
        String shareText = buildShareText(context, image);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TITLE, "Hello World");
        if (shareText != null) {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        intent.setType(MIME_IMAGE);
        context.startActivity(Intent.createChooser(
                intent,
                context.getResources().getText(R.string.title_share_photo)
        ));

    }

    private String buildShareText(Context context, Image image) {
        User user = image.getUser();
        String shareText = null;
        if (user != null && user.getName() != null) {
            String imageUrl = image.getShareUrl().value();
            String userName = user.getName().value();
            String userUrl = null;
            if (user.getProfileUrl() != null) {
                userUrl = user.getProfileUrl().value();
            }
            if (userUrl == null && user.getPortfolioUrl() != null) {
                userUrl = user.getPortfolioUrl().value();
            }

            if (userUrl != null) {
                shareText = context.getString(
                        R.string.share_file_template,
                        imageUrl,
                        userName,
                        userUrl
                );
            } else {
                shareText = context.getString(
                        R.string.share_file_template_nouserurl,
                        imageUrl,
                        userName
                );
            }
        }
        return shareText;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
