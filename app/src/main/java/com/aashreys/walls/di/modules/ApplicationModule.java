package com.aashreys.walls.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.device.DeviceResolutionImpl;
import com.aashreys.walls.domain.display.images.utils.ImageCache;
import com.aashreys.walls.domain.display.images.utils.ImageCacheImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 17/02/17.
 */

@Module
public class ApplicationModule {

    private final WallsApplication application;

    private final ImageCache imageCache;

    public ApplicationModule(WallsApplication application) {
        this.application = application;
        this.imageCache = new ImageCacheImpl();
    }

    @Provides
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    public ImageCache providesMemoryCache() {
        return imageCache;
    }

    @Provides
    public DeviceResolution providesDeviceResolution() {
        return new DeviceResolutionImpl(application);
    }

}
