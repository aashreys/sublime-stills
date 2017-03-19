package com.aashreys.walls.domain.share;

import com.aashreys.walls.domain.device.DeviceResolution;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/02/17.
 */

public class SharerFactory {

    private final ImageUrlSharerFactory imageUrlSharerFactory;

    private final DeviceResolution deviceResolution;

    @Inject
    public SharerFactory(
            ImageUrlSharerFactory imageUrlSharerFactory,
            DeviceResolution deviceResolution
    ) {
        this.imageUrlSharerFactory = imageUrlSharerFactory;
        this.deviceResolution = deviceResolution;
    }

    public Sharer create(Sharer.ShareMode shareMode) {
        switch (shareMode) {
            case ONLY_URL:
                return imageUrlSharerFactory.create();
            case SET_AS:
                return new SetAsSharer(deviceResolution);
        }
        throw new IllegalArgumentException("Unexpected share mode");
    }
}
