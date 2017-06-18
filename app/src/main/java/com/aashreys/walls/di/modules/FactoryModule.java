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

import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.display.sources.FavoriteSourceFactory;
import com.aashreys.walls.domain.display.sources.SourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashCollectionSourceFactory;
import com.aashreys.walls.domain.display.sources.UnsplashRecentSourceFactory;
import com.aashreys.walls.domain.share.ShareDelegateFactory;
import com.aashreys.walls.domain.share.actions.ShareActionFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 18/02/17.
 */

@Module
public class FactoryModule {

    public FactoryModule() {}

    @Provides
    public SourceFactory providesSourceFactory(
            UnsplashCollectionSourceFactory unsplashCollectionSourceFactory,
            UnsplashRecentSourceFactory unsplashRecentSourceFactory,
            FavoriteSourceFactory favoriteSourceFactory
    ) {
        return new SourceFactory(
                unsplashCollectionSourceFactory,
                unsplashRecentSourceFactory,
                favoriteSourceFactory
        );
    }

    @Provides
    public ShareDelegateFactory providesSharerFactory(
            DeviceInfo deviceInfo,
            ShareActionFactory shareActionFactory,
            ImageDownloader imageDownloader,
            SchedulerProvider schedulerProvider
    ) {
        return new ShareDelegateFactory(
                deviceInfo.getDeviceResolution(),
                shareActionFactory,
                imageDownloader,
                schedulerProvider
        );
    }

}
