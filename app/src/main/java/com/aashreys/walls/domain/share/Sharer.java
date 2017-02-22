package com.aashreys.walls.domain.share;

import android.content.Context;

import com.aashreys.walls.domain.display.images.Image;

/**
 * Created by aashreys on 03/12/16.
 */

public interface Sharer {

    void share(Context context, Image image, Listener listener);

    void cancel();

    enum ShareMode {
        ONLY_URL,
        SET_AS
    }

    interface Listener {

        void onShareComplete();

        void onShareFailed();

    }

}
