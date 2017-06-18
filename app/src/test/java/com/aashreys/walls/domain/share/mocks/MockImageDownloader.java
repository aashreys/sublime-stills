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

package com.aashreys.walls.domain.share.mocks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.domain.values.Url;
import com.bumptech.glide.Priority;

import java.io.File;

import io.reactivex.Single;

/**
 * Created by aashreys on 06/04/17.
 */

public class MockImageDownloader extends ImageDownloader {

    private boolean shouldFail;

    private File mockFile;

    private Drawable mockDrawable;

    private Exception mockException;

    public void setMockFile(File mockFile) {
        this.mockFile = mockFile;
    }

    public void setMockDrawable(Drawable mockDrawable) {
        this.mockDrawable = mockDrawable;
    }

    public void setMockException(Exception mockException) {
        this.mockException = mockException;
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    @Override
    public Single<File> asFile(Context context, Url url) {
        if (!shouldFail) {
            return Single.just(mockFile);
        } else {
            return Single.error(mockException);
        }
    }

    @Override
    public Single<Drawable> asDrawable(
            Context context,
            Url url,
            Priority priority,
            ImageView imageView
    ) {
        if (!shouldFail) {
            return Single.just(mockDrawable);
        } else {
            return Single.error(mockException);
        }
    }

    @Override
    public Single<Drawable> asDrawable(
            Fragment fragment,
            Url url,
            Priority priority,
            ImageView imageView
    ) {
        if (!shouldFail) {
            return Single.just(mockDrawable);
        } else {
            return Single.error(mockException);
        }
    }
}
