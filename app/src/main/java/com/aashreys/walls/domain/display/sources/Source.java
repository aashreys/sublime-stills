package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.aashreys.walls.domain.display.images.Image;

import java.util.List;

/**
 * Created by aashreys on 21/11/16.
 */

public interface Source {

    @WorkerThread
    @NonNull
    List<Image> getImages(int fromIndex);
}
