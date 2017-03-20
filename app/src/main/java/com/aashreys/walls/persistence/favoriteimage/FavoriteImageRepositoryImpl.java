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

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * Created by aashreys on 06/12/16.
 */

public class FavoriteImageRepositoryImpl implements FavoriteImageRepository {

    private static final String TAG = FavoriteImageRepositoryImpl.class.getSimpleName();

    private static final String BOOK_NAME = "favorite_image_book";

    private static List<RepositoryCallback<Image>> listeners = new ArrayList<>();

    @Override
    public void favorite(Image image) {
        getBook().write(getImageKey(image), image);
        _notifyFavorited(image);
    }

    @Override
    public void unfavorite(Image image) {
        getBook().delete(getImageKey(image));
        _notifyUnfavorited(image);
    }

    @Override
    public boolean isFavorite(Image image) {
        return getBook().exist(getImageKey(image));
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
                Image image = book.read(keys.get(i), null);
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

    private String getImageKey(Image image) {
        return image.getType() + image.getId().value();
    }
}
