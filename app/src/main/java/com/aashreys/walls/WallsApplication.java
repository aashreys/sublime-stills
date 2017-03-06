package com.aashreys.walls;

import android.app.Application;
import android.content.SharedPreferences;

import com.aashreys.walls.di.ApplicationComponent;
import com.aashreys.walls.di.DaggerApplicationComponent;
import com.aashreys.walls.di.modules.ApiModule;
import com.aashreys.walls.di.modules.ApplicationModule;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * Created by aashreys on 30/11/16.
 */

public class WallsApplication extends Application {

    @Inject SharedPreferences sharedPreferences;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable crash reporting for production only.
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(!BuildConfig.PRODUCTION).build())
                .build();
        Fabric.with(this, crashlyticsKit);
        LogWrapper.setProductionLogging(BuildConfig.PRODUCTION);
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
        this.applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule(this))
                .build();
        this.applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}
