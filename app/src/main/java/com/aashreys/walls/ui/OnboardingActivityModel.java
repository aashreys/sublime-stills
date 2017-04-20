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

package com.aashreys.walls.ui;

import android.support.annotation.DimenRes;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTaskFactory;
import com.aashreys.walls.ui.views.ChipView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/04/17.
 */

public class OnboardingActivityModel implements ViewModel, ChipView.OnCheckedListener,
        CollectionSearchTask.CollectionSearchListener {

    private final List<Collection> checkedCollectionList;

    @Inject FeaturedCollectionsTaskFactory collectionsTaskFactory;

    @Inject KeyValueStore keyValueStore;

    @Inject CollectionRepository collectionRepository;

    private EventListener eventListener;

    private FeaturedCollectionsTask collectionsTask;

    private int chipViewHeight, chipViewMargin;

    @Inject
    public OnboardingActivityModel() {
        checkedCollectionList = new ArrayList<>();
    }

    @DimenRes
    int getChipViewMargin() {
        return R.dimen.spacing_xs;
    }

    @DimenRes
    int getChipViewHeight() {
        return R.dimen.height_small;
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    ChipView.OnCheckedListener getChipViewOnCheckedListener() {
        return this;
    }

    @Override
    public void onChipViewChecked(Collection checkedCollection) {
        if (checkedCollection != null) {
            checkedCollectionList.add(checkedCollection);
        }
    }

    @Override
    public void onChipViewUnchecked(Collection uncheckedCollection) {
        if (uncheckedCollection != null) {
            checkedCollectionList.remove(uncheckedCollection);
        }
    }

    private void loadCuratedCollections() {
        if (collectionsTask != null) {
            collectionsTask.cancel(true);
        }
        collectionsTask = collectionsTaskFactory.create();
        collectionsTask.setListener(this);
        collectionsTask.execute();
    }

    void onActivityReady() {
        loadCuratedCollections();
    }

    boolean isCollectionChecked(Collection collection) {
        return checkedCollectionList.contains(collection);
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList) {
        if (eventListener != null) {
            eventListener.onSearchComplete(collectionList);
        }
    }

    void onContinueButtonClicked() {
        saveCollections();
        markOnboardingAsComplete();

    }

    private void saveCollections() {
        for (Collection collection : checkedCollectionList) {
            collectionRepository.insert(collection);
        }
    }

    private void markOnboardingAsComplete() {
        keyValueStore.putBoolean(StreamActivityModel.KEY_IS_ONBOARDING_COMPLETED, true);
        if (eventListener != null) {
            eventListener.onOnboardingComplete();
        }
    }

    void onInjectionComplete() {

    }


    interface EventListener {

        void onSearchComplete(List<Collection> collectionList);

        void onOnboardingComplete();

    }

}
