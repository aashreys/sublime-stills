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

package com.aashreys.walls.ui.tasks;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;

import java.io.IOException;
import java.util.List;

/**
 * Created by aashreys on 09/02/17.
 */
public class LoadImagesTask extends AsyncTask<Integer, Void, List<Image>> {

    private Source source;

    private LoadCallback listener;

    private boolean isReleased;

    private boolean isError;

    public LoadImagesTask(
            @NonNull Source source,
            @NonNull LoadCallback listener
    ) {
        this.source = source;
        this.listener = listener;
    }

    @Override
    protected List<Image> doInBackground(Integer... fromIndex) {
        if (!isReleased) {
            try {
                return source.getImages(fromIndex[0]);
            } catch (IOException e) {
                isError = true;
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Image> images) {
        super.onPostExecute(images);
        if (!isReleased) {
            if (!isError) {
                listener.onLoadComplete(images);
            } else {
                listener.onLoadError();
            }
        }
    }

    public boolean isLoading() {
        return getStatus() == Status.RUNNING;
    }

    public void release() {
        cancel(true);
        source = null;
        listener = null;
        isError = false;
        isReleased = true;
    }

    public interface LoadCallback {

        @MainThread
        void onLoadComplete(@NonNull List<Image> images);

        @MainThread
        void onLoadError();

    }
}
