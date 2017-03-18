package com.aashreys.walls.di;

import com.aashreys.walls.di.modules.ApplicationModule;
import com.aashreys.walls.di.modules.FactoryModule;
import com.aashreys.walls.di.modules.RepositoryModule;
import com.aashreys.walls.di.modules.ServiceModule;
import com.aashreys.walls.di.scopes.UiScoped;
import com.aashreys.walls.ui.AddCollectionDialog;
import com.aashreys.walls.ui.CollectionsActivity;
import com.aashreys.walls.ui.ImageDetailActivity;
import com.aashreys.walls.ui.ImageStreamFragment;
import com.aashreys.walls.ui.StreamActivity;
import com.aashreys.walls.ui.views.CollectionView;
import com.aashreys.walls.ui.views.HintView;
import com.aashreys.walls.ui.views.StreamImageView;

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

    void inject(ImageStreamFragment imageStreamFragment);

    void inject(ImageDetailActivity imageDetailActivity);

    void inject(CollectionsActivity collectionsActivity);

    void inject(AddCollectionDialog addCollectionDialog);

    void inject(CollectionView collectionView);

    void inject(StreamImageView streamImageView);

    void inject(HintView hintView);

}
