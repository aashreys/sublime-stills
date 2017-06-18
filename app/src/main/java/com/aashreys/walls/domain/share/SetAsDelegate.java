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

import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.actions.SetAsAction;
import com.aashreys.walls.domain.values.Url;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by aashreys on 05/12/16.
 */

public class SetAsDelegate implements ShareDelegate {

    private static final String TAG = SetAsDelegate.class.getSimpleName();

    private final DeviceResolution deviceResolution;

    private final ImageDownloader imageDownloader;

    private final SetAsAction setAsAction;

    private final SchedulerProvider schedulerProvider;

    public SetAsDelegate(
            DeviceResolution deviceResolution,
            SetAsAction setAsAction,
            ImageDownloader imageDownloader,
            SchedulerProvider schedulerProvider
    ) {
        this.deviceResolution = deviceResolution;
        this.setAsAction = setAsAction;
        this.imageDownloader = imageDownloader;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Completable share(final Context context, final Image image) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(
                    @NonNull final CompletableEmitter completableEmitter
            ) throws Exception {
                final Url imageUrl = image.getUrl(deviceResolution.getWidth() * 2);
                imageDownloader.asFile(context, imageUrl)
                        .subscribeOn(schedulerProvider.mainThread())
                        .observeOn(schedulerProvider.mainThread())
                        .subscribe(new SingleObserver<File>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable disposable) {
                            }

                            @Override
                            public void onSuccess(@NonNull File file) {
                                if (!completableEmitter.isDisposed()) {
                                    setAsAction.setAs(context, file);
                                    completableEmitter.onComplete();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable throwable) {
                                if (!completableEmitter.isDisposed()) {
                                    completableEmitter.onError(throwable);
                                }
                            }
                        });
            }
        });
    }
}
