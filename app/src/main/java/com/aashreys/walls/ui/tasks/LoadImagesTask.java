package com.aashreys.walls.ui.tasks;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.sources.Source;

import java.util.List;

/**
 * Created by aashreys on 09/02/17.
 */
public class LoadImagesTask extends AsyncTask<Integer, Void, List<Image>> {

    private Source source;

    private ImageLoadCallback listener;

    private boolean isReleased;

    public LoadImagesTask(
            @NonNull Source source,
            @NonNull ImageLoadCallback listener
    ) {
        this.source = source;
        this.listener = listener;
    }

    @Override
    protected List<Image> doInBackground(Integer... fromIndex) {
        if (!isReleased) {
            return source.getImages(fromIndex[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Image> images) {
        super.onPostExecute(images);
        if (!isReleased) {
            listener.onLoadComplete(images);
        }
    }

    public boolean isLoading() {
        return getStatus() == Status.RUNNING;
    }

    public void release() {
        cancel(true);
        source = null;
        listener = null;
        isReleased = true;
    }

    public interface ImageLoadCallback {

        @MainThread
        void onLoadComplete(@NonNull List<Image> images);

    }
}
