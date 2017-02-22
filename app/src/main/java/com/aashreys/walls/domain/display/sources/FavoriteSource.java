package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

/**
 * Created by aashreys on 06/12/16.
 */

@AutoFactory
public class FavoriteSource implements Source {

    private static final int ITEMS_PER_PAGE = 30;

    private final FavoriteImageRepository favoriteImageRepository;

    FavoriteSource(@Provided FavoriteImageRepository favoriteImageRepository) {
        this.favoriteImageRepository = favoriteImageRepository;
    }

    @NonNull
    @Override
    public List<Image> getImages(int fromIndex) {
        return favoriteImageRepository.get(fromIndex, ITEMS_PER_PAGE);
    }

}
