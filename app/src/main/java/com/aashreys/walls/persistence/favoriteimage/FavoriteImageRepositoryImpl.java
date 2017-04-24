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

package com.aashreys.walls.persistence.favoriteimage;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.RepositoryCallback;
import com.aashreys.walls.persistence.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * Created by aashreys on 06/12/16.
 */

public class FavoriteImageRepositoryImpl implements FavoriteImageRepository {

    private static final String TAG = FavoriteImageRepositoryImpl.class.getSimpleName();

    public static final String BOOK_NAME = "favorite_image_book";

    private static List<RepositoryCallback<Image>> listeners = new ArrayList<>();

    private final ImageModelFactory imageModelFactory;

    public FavoriteImageRepositoryImpl(ImageModelFactory imageModelFactory) {
        this.imageModelFactory = imageModelFactory;
    }

    @Override
    public void favorite(Image image) {
        ImageModel model = createModel(image);
        getBook().write(getModelKey(model), model);
        _notifyFavorited(image);
    }

    @Override
    public void unfavorite(Image image) {
        ImageModel model = createModel(image);
        getBook().delete(getModelKey(model));
        _notifyUnfavorited(image);
    }

    @Override
    public boolean isFavorite(Image image) {
        ImageModel model = createModel(image);
        return getBook().exist(getModelKey(model));
    }

    @Override
    public void addListener(RepositoryCallback<Image> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(RepositoryCallback<Image> listener) {
        listeners.remove(listener);
    }

    @NonNull
    @Override
    public List<Image> get(int fromIndex, int count) {
        List<Image> imageList = new ArrayList<>();
        List<String> keys = getBook().getAllKeys();
        if (keys.size() > 0) {
            int size = (fromIndex + count) > keys.size() ? keys.size() : (fromIndex + count);
            Book book = getBook();
            for (int i = fromIndex; i < size; i++) {
                ImageModel model = book.read(keys.get(i), null);
                Image image = model.createImage();
                if (image != null) {
                    imageList.add(image);
                }
            }
        }
        return imageList;
    }

    private void _notifyUnfavorited(Image image) {
        if (listeners.size() > 0) {
            for (RepositoryCallback<Image> listener : listeners) {
                listener.onDelete(image);
            }
        }
    }

    private void _notifyFavorited(Image image) {
        if (listeners.size() > 0) {
            for (RepositoryCallback<Image> listener : listeners) {
                listener.onInsert(image);
            }
        }
    }

    private Book getBook() {
        return Paper.book(BOOK_NAME);
    }

    private ImageModel createModel(Image image) {
        return imageModelFactory.create(image);
    }

    private String getModelKey(ImageModel model) {
        return model.getClass().getSimpleName() + model.getId();
    }
}
