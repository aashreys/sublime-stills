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
import android.preference.PreferenceManager;

import com.aashreys.maestro.ViewModelStore;
import com.aashreys.walls.application.Migrator;
import com.aashreys.walls.application.WallsApplication;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.device.DeviceInfoImpl;
import com.aashreys.walls.domain.device.ResourceProvider;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.application.helpers.NetworkHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 17/02/17.
 */

@Module
public class ApplicationModule {

    private final WallsApplication application;

    private final ViewModelStore viewModelStore;

    private final DeviceInfo deviceInfo;

    private final ResourceProvider resourceProvider;

    public ApplicationModule(WallsApplication application) {
        this.application = application;
        this.viewModelStore = new ViewModelStore();
        this.deviceInfo = new DeviceInfoImpl(application);
        this.resourceProvider = new ResourceProvider(application);
    }

    @Provides
    public Migrator providesMigrator(KeyValueStore keyValueStore) {
        return new Migrator(application, keyValueStore);
    }

    @Provides
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    public DeviceInfo providesDeviceInfo() {
        return deviceInfo;
    }

    @Provides
    public ViewModelStore providesViewModelStore() {
        return viewModelStore;
    }

    @Provides
    public ResourceProvider providesResourceProvider() {
        return resourceProvider;
    }

    @Provides
    public NetworkHelper providesNetworkHelper() {
        return new NetworkHelper(application);
    }

}
