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

/**
 * Created by aashreys on 06/04/17.
 */

public class ImageDownloader {

    @Inject
    public ImageDownloader() {}

    public void asFile(Context context, Url url, final Listener<File> listener) {
        RequestManager requestManager = getRequestManagerFromContext(context);
        requestManager.load(url.value())
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }

                    @Override
                    public void onResourceReady(
                            File resource, GlideAnimation<? super File> glideAnimation
                    ) {
                        if (listener != null) {
                            listener.onComplete(resource);
                        }
                    }
                });
    }

    public void asDrawable(
            Context context,
            Url url,
            Priority priority,
            ImageView imageView,
            Listener<Drawable> listener
    ) {
        configureRequestManagerToLoadImage(
                getRequestManagerFromContext(context),
                url,
                priority,
                imageView,
                listener
        );
    }

    public void asDrawable(
            Fragment fragment,
            Url url,
            Priority priority,
            ImageView imageView,
            final Listener<Drawable> listener
    ) {
        configureRequestManagerToLoadImage(
                Glide.with(fragment),
                url,
                priority,
                imageView,
                listener
        );
    }

    private void configureRequestManagerToLoadImage(
            RequestManager requestManager,
            Url url,
            Priority priority,
            ImageView imageView,
            final Listener<Drawable> listener
    ) {
        requestManager.load(url.value())
                .priority(priority)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(
                            Exception e,
                            String model,
                            Target<GlideDrawable> target,
                            boolean isFirstResource
                    ) {
                        if (listener != null) {
                            listener.onError(e);
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
                        if (listener != null) {
                            listener.onComplete(resource);
                        }
                        return true;
                    }
                })
                .into(imageView);
    }

    private RequestManager getRequestManagerFromContext(Context context) {
        RequestManager requestManager;
        if (context instanceof Activity) {
            requestManager = Glide.with((Activity) context);
        } else {
            requestManager = Glide.with(context);
        }
        return requestManager;
    }

    public interface Listener<T> {

        void onComplete(T result);

        void onError(Exception e);

    }

}
