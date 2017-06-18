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

package com.aashreys.walls.application.activities;

import android.support.annotation.DimenRes;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.application.views.ChipView;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.search.CollectionDiscoveryService;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by aashreys on 17/04/17.
 */

public class OnboardingActivityModel implements ViewModel, ChipView.OnCheckedListener {

    private final List<Collection> checkedCollectionList;

    private final KeyValueStore keyValueStore;

    private final CollectionDiscoveryService collectionDiscoveryService;

    private final CollectionRepository collectionRepository;

    private Disposable collectionObserver;

    private EventListener eventListener;

    private final SchedulerProvider schedulerProvider;

    @Inject
    OnboardingActivityModel(
            KeyValueStore keyValueStore,
            CollectionDiscoveryService collectionDiscoveryService,
            CollectionRepository collectionRepository,
            SchedulerProvider schedulerProvider
    ) {
        this.keyValueStore = keyValueStore;
        this.collectionDiscoveryService = collectionDiscoveryService;
        this.collectionRepository = collectionRepository;
        this.schedulerProvider = schedulerProvider;
        this.checkedCollectionList = new ArrayList<>();
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

    private void loadFeaturedCollections() {
        disposeCollectionObserver();
        collectionDiscoveryService.getFeatured()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(new SingleObserver<List<Collection>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        collectionObserver = disposable;
                    }

                    @Override
                    public void onSuccess(@NonNull List<Collection> collectionList) {
                        onSearchComplete(collectionList);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        onSearchComplete(new ArrayList<Collection>());
                    }
                });
    }

    private void disposeCollectionObserver() {
        if (collectionObserver != null && !collectionObserver.isDisposed()) {
            collectionObserver.dispose();
        }
    }

    void onActivityReady() {
        loadFeaturedCollections();
    }

    boolean isCollectionChecked(Collection collection) {
        return checkedCollectionList.contains(collection);
    }

    private void onSearchComplete(List<Collection> collectionList) {
        if (eventListener != null) {
            eventListener.onSearchComplete(collectionList);
        }
    }

    void onContinueButtonClicked() {
        saveCollections();
        saveOnboardingComplete();

    }

    private void saveCollections() {
        for (Collection collection : checkedCollectionList) {
            collectionRepository.insert(collection);
        }
    }

    private void saveOnboardingComplete() {
        keyValueStore.putBoolean(StreamActivityModel.KEY_IS_ONBOARDING_COMPLETED, true);
        if (eventListener != null) {
            eventListener.onOnboardingComplete();
        }
    }

    void onActivityDestroyed() {
        disposeCollectionObserver();
    }

    interface EventListener {

        void onSearchComplete(List<Collection> collectionList);

        void onOnboardingComplete();

    }

}
