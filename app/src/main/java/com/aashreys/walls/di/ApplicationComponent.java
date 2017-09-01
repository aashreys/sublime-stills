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

package com.aashreys.walls.di;

import com.aashreys.maestro.ViewModelStore;
import com.aashreys.walls.application.WallsApplication;
import com.aashreys.walls.di.modules.ApiModule;
import com.aashreys.walls.di.modules.ApplicationModule;
import com.aashreys.walls.di.modules.RepositoryModule;
import com.aashreys.walls.di.modules.ServiceModule;
import com.aashreys.walls.di.scopes.ApplicationScoped;

import dagger.Component;

/**
 * Created by aashreys on 17/02/17.
 */

@ApplicationScoped
@Component(modules = {ApplicationModule.class, ApiModule.class, RepositoryModule.class, ServiceModule.class})
public interface ApplicationComponent {

    void inject(WallsApplication application);

    UiComponent getUiComponent();

    ViewModelStore viewModelStore();

}
