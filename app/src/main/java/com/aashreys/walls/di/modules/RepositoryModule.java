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

package com.aashreys.walls.di.modules;

import android.content.SharedPreferences;

import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.KeyValueStoreImpl;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.persistence.collections.CollectionRepositoryImpl;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepositoryImpl;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepository;
import com.aashreys.walls.persistence.shorturl.ShortUrlRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class RepositoryModule {

    public RepositoryModule() {}

    @Provides
    public CollectionRepository providesCollectionRepository(CollectionFactory collectionFactory) {
        return new CollectionRepositoryImpl(collectionFactory);
    }

    @Provides
    public FavoriteImageRepository providesFavoriteImageRepository() {
        return new FavoriteImageRepositoryImpl();
    }

    @Provides
    public ShortUrlRepository providesShortUrlRepository() {
        return new ShortUrlRepositoryImpl();
    }

    @Provides
    public KeyValueStore providesKeyValueStore(SharedPreferences sharedPreferences) {
        return new KeyValueStoreImpl(sharedPreferences);
    }

}
