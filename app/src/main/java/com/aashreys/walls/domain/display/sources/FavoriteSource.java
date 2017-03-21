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

package com.aashreys.walls.domain.display.sources;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.io.IOException;
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
    public List<Image> getImages(int fromIndex) throws IOException {
        return favoriteImageRepository.get(fromIndex, ITEMS_PER_PAGE);
    }

}
