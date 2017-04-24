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

import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.actions.ShareImageAction;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.application.helpers.UiHandler;

import java.io.File;

/**
 * Created by aashreys on 20/03/17.
 */

public class ShareImageDelegate implements ShareDelegate {

    private static final int minWidth = 720;

    private final ShareImageAction shareImageAction;

    private final DeviceResolution deviceResolution;

    private final UiHandler uiHandler;

    private final ImageDownloader imageDownloader;

    private ImageFileDownloadListener imageFileDownloadListener;

    public ShareImageDelegate(
            DeviceResolution deviceResolution,
            ShareImageAction shareImageAction,
            UiHandler uiHandler,
            ImageDownloader imageDownloader
    ) {
        this.deviceResolution = deviceResolution;
        this.shareImageAction = shareImageAction;
        this.uiHandler = uiHandler;
        this.imageDownloader = imageDownloader;
    }

    @Override
    public void share(
            final Context context, final Image image, final Listener listener
    ) {
        imageFileDownloadListener = createImageFileDownloadListener(context, image, listener);
        int width = deviceResolution.getWidth() > minWidth ?
                deviceResolution.getWidth() :
                minWidth;
        final Url imageUrl = image.getUrl(width);

        imageDownloader.asFile(context, imageUrl, imageFileDownloadListener);
    }

    private ImageFileDownloadListener createImageFileDownloadListener(
            final Context context,
            final Image image,
            final Listener listener
    ) {
        return new ImageFileDownloadListener() {
            @Override
            public void onComplete(File result) {
                if (!isCancelled) {
                    shareImageAction.shareImage(context, image, result);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onShareComplete();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                if (!isCancelled) {
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
        if (imageFileDownloadListener != null) {
            imageFileDownloadListener.isCancelled = true;
        }
    }
}
