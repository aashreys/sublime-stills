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

package com.aashreys.walls.application.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aashreys.walls.domain.values.Url;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by aashreys on 06/04/17.
 */

public class ImageDownloader {

    @Inject
    public ImageDownloader() {}

    public Single<File> asFile(final Context context, final Url url) {
        return Single.create(new SingleOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<File> singleEmitter) throws
                    Exception {
                getRequestManager(context)
                        .load(url.value())
                        .downloadOnly(new SimpleTarget<File>() {
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                if (!singleEmitter.isDisposed()) {
                                    singleEmitter.onError(e);
                                }
                            }

                            @Override
                            public void onResourceReady(
                                    File file, GlideAnimation<? super File> glideAnimation
                            ) {
                                if (!singleEmitter.isDisposed()) {
                                    singleEmitter.onSuccess(file);
                                }
                            }
                        });
            }
        });
    }

    public Single<Drawable> asDrawable(
            Context context,
            Url url,
            Priority priority,
            ImageView imageView
    ) {
        return createDownloadSingle(
                getRequestManager(context),
                url,
                priority,
                imageView
        );
    }

    public Single<Drawable> asDrawable(
            Fragment fragment,
            Url url,
            Priority priority,
            ImageView imageView
    ) {
        return createDownloadSingle(
                Glide.with(fragment),
                url,
                priority,
                imageView
        );
    }

    private Single<Drawable> createDownloadSingle(
            final RequestManager requestManager,
            final Url url,
            final Priority priority,
            final ImageView imageView
    ) {
        return Single.create(
                new SingleOnSubscribe<Drawable>() {
                    @Override
                    public void subscribe(
                            @NonNull final SingleEmitter<Drawable> singleEmitter
                    ) throws Exception {
                        requestManager.load(url.value())
                                .priority(priority)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(
                                            Exception e,
                                            String model,
                                            Target<GlideDrawable> target,
                                            boolean isFirstResource
                                    ) {
                                        if (!singleEmitter.isDisposed()) {
                                            singleEmitter.onError(e);
                                        }
                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(
                                            GlideDrawable resource,
                                            String model,
                                            Target<GlideDrawable> target,
                                            boolean isFromMemoryCache,
                                            boolean isFirstResource
                                    ) {
                                        if (!singleEmitter.isDisposed()) {
                                            singleEmitter.onSuccess(resource);
                                        }
                                        return true;
                                    }
                                })
                                .into(imageView);
                    }
                }
        );
    }

    private RequestManager getRequestManager(Context context) {
        RequestManager requestManager;
        if (context instanceof Activity) {
            requestManager = Glide.with((Activity) context);
        } else {
            requestManager = Glide.with(context);
        }
        return requestManager;
    }

}
