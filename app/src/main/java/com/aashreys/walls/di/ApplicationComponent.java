package com.aashreys.walls.di;

import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.di.modules.ApiModule;
import com.aashreys.walls.di.modules.ApplicationModule;
import com.aashreys.walls.di.scopes.ApplicationScoped;

import dagger.Component;

/**
 * Created by aashreys on 17/02/17.
 */

@ApplicationScoped
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(WallsApplication application);

    UiComponent getUiComponent();

}
