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

import android.app.Application;

import com.aashreys.walls.application.WallsApplication;
import com.aashreys.walls.domain.display.collections.search.CollectionDiscoveryService;
import com.aashreys.walls.domain.display.collections.search.CollectionDiscoveryServiceImpl;
import com.aashreys.walls.domain.display.collections.search.UnsplashCollectionDiscoveryService;
import com.aashreys.walls.domain.display.images.ImageService;
import com.aashreys.walls.domain.display.images.ImageServiceImpl;
import com.aashreys.walls.domain.preferences.PreferenceService;
import com.aashreys.walls.domain.preferences.PreferenceServiceImpl;
import com.aashreys.walls.network.unsplash.UnsplashApi;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class ServiceModule {

    private final Application application;

    public ServiceModule(WallsApplication application) {
        this.application = application;
    }

    @Provides
    public ImageService providesImageService(UnsplashApi unsplashApi) {
        return new ImageServiceImpl(unsplashApi);
    }

    @Provides
    public CollectionDiscoveryService providesCollectionDiscoveryService(
            UnsplashCollectionDiscoveryService unsplashCollectionSearchService
    ) {
        return new CollectionDiscoveryServiceImpl(unsplashCollectionSearchService);
    }

    @Provides
    public PreferenceService providesPreferenceService() {
        return new PreferenceServiceImpl(application);
    }

}
