package com.aashreys.walls.persistence.favoriteimage;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.RepositoryCallback;

import java.util.List;

/**
 * Created by aashreys on 06/12/16.
 */

public interface FavoriteImageRepository {

    void favorite(Image image);

    void unfavorite(Image image);

    boolean isFavorite(Image image);

    void addListener(RepositoryCallback<Image> listener);

    void removeListener(RepositoryCallback<Image> listener);

    @NonNull
    List<Image> get(int fromIndex, int count);

}
