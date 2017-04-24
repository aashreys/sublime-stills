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
import com.aashreys.walls.domain.share.actions.SetAsAction;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.application.helpers.UiHandler;

import java.io.File;

/**
 * Created by aashreys on 05/12/16.
 */

public class SetAsDelegate implements ShareDelegate {

    private static final String TAG = SetAsDelegate.class.getSimpleName();

    private final DeviceResolution deviceResolution;

    private final UiHandler uiHandler;

    private final ImageDownloader imageDownloader;

    private SetAsAction setAsAction;

    private ImageFileDownloadListener imageFileDownloadListener;

    public SetAsDelegate(
            DeviceResolution deviceResolution,
            SetAsAction setAsAction,
            UiHandler uiHandler,
            ImageDownloader imageDownloader
    ) {
        this.deviceResolution = deviceResolution;
        this.setAsAction = setAsAction;
        this.uiHandler = uiHandler;
        this.imageDownloader = imageDownloader;
    }

    @Override
    public void share(final Context context, final Image image, final Listener listener) {
        imageFileDownloadListener = createImageFileDownloadListener(context, listener);
        final Url imageUrl = image.getUrl(deviceResolution.getWidth() * 2);
        imageDownloader.asFile(context, imageUrl, imageFileDownloadListener);
    }

    private ImageFileDownloadListener createImageFileDownloadListener(
            final Context context,
            final Listener listener
    ) {
        return new ImageFileDownloadListener() {
            @Override
            public void onComplete(File result) {
                if (!isCancelled) {
                    setAsAction.setAs(context, result);
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
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onShareFailed();
                    }
                });
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
