package com.aashreys.walls.domain.share;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/02/17.
 */

public class SharerFactory {

    private final ImageUrlSharerFactory imageUrlSharerFactory;

    @Inject
    public SharerFactory(ImageUrlSharerFactory imageUrlSharerFactory) {
        this.imageUrlSharerFactory = imageUrlSharerFactory;
    }

    public Sharer create(Sharer.ShareMode shareMode) {
        switch (shareMode) {
            case ONLY_URL:
                return imageUrlSharerFactory.create();
            case SET_AS:
                return new SetAsSharer();
        }
        throw new IllegalArgumentException("Unexpected share mode");
    }
}
