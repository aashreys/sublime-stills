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
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.actions.ShareImageAction;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.ui.helpers.GlideHelper;
import com.aashreys.walls.ui.utils.UiHandler;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by aashreys on 20/03/17.
 */

public class ShareImageDelegate implements ShareDelegate {

    private static final int minWidth = 720;

    private final ShareImageAction shareImageAction;

    private final DeviceResolution deviceResolution;

    private final UiHandler uiHandler = new UiHandler();

    private boolean isCancelled;

    ShareImageDelegate(DeviceResolution deviceResolution, ShareImageAction shareImageAction) {
        this.deviceResolution = deviceResolution;
        this.shareImageAction = shareImageAction;
    }

    @Override
    public void share(
            final Context context, final Image image, final Listener listener
    ) {
        isCancelled = false;
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
                        if (!isCancelled) {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onShareFailed();
                                }
                            });
                        }
                    }

                    @Override
                    public void onResourceReady(
                            File resource, GlideAnimation<? super File> glideAnimation
                    ) {
                        if (!isCancelled) {
                            // Glide has been configured to use an external cache so that cached
                            // images are shareable by default. See {@link @GlideConfiguration}.
                            Uri imageUri = Uri.fromFile(resource);
                            shareImageAction.shareImage(context, image, imageUri);
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
