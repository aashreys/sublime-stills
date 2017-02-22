package com.aashreys.walls.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aashreys.walls.WallsApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aashreys on 17/02/17.
 */

@Module
public class ApplicationModule {

    private WallsApplication application;

    public ApplicationModule(WallsApplication application) {
        this.application = application;
    }

    @Provides
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
