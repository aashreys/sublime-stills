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

import com.aashreys.walls.di.modules.ApplicationModule;
import com.aashreys.walls.di.modules.FactoryModule;
import com.aashreys.walls.di.modules.RepositoryModule;
import com.aashreys.walls.di.modules.ServiceModule;
import com.aashreys.walls.di.scopes.UiScoped;
import com.aashreys.walls.ui.AddCollectionsActivityModel;
import com.aashreys.walls.ui.CollectionsActivityModel;
import com.aashreys.walls.ui.ImageDetailActivity;
import com.aashreys.walls.ui.OnboardingActivity;
import com.aashreys.walls.ui.SettingsActivity;
import com.aashreys.walls.ui.StreamActivity;
import com.aashreys.walls.ui.StreamActivityViewModel;
import com.aashreys.walls.ui.StreamFragment;
import com.aashreys.walls.ui.views.CollectionViewModel;
import com.aashreys.walls.ui.views.HintViewModel;
import com.aashreys.walls.ui.views.StreamImageViewModel;

import dagger.Subcomponent;

/**
 * Created by aashreys on 18/02/17.
 */

@UiScoped
@Subcomponent(modules = {
        ApplicationModule.class,
        RepositoryModule.class,
        FactoryModule.class,
        ServiceModule.class
})
public interface UiComponent {

    void inject(StreamActivity activity);

    void inject(StreamFragment streamFragment);

    void inject(ImageDetailActivity imageDetailActivity);

    void inject(CollectionsActivityModel collectionsActivityModel);

    void inject(CollectionViewModel collectionViewModel);

    void inject(StreamImageViewModel streamImageViewModel);

    void inject(HintViewModel hintViewModel);

    void inject(SettingsActivity settingsActivity);

    void inject(OnboardingActivity onboardingActivity);

    void inject(AddCollectionsActivityModel addCollectionsActivityModel);

    void inject(StreamActivityViewModel vm);
}
