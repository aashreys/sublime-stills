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

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.CollectionSearchTaskFactory;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTaskFactory;
import com.aashreys.walls.ui.views.ChipView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 12/04/17.
 */

public class AddCollectionsActivityModel implements ViewModel, CollectionSearchTask
        .CollectionSearchListener, ChipView.OnCheckedListener {

    private static final int MIN_COLLECTION_SIZE = 15;

    private static final int MIN_COLLECTION_NAME_LENGTH = 3;

    private final SelectedCollectionHolder selectedCollectionHolder;

    @DimenRes
    private final int chipViewHeight, chipViewMargin;

    private final ActionButtonClickDelegate
            searchCollectionsDelegate,
            saveCollectionsDelegate;

    private final int actionButtonSearchText, actionButtonAddText;

    @Inject FeaturedCollectionsTaskFactory featuredCollectionsTaskFactory;

    @Inject CollectionSearchTaskFactory collectionSearchTaskFactory;

    @Inject CollectionRepository collectionRepository;

    private FeaturedCollectionsTask featuredCollectionsTask;

    private CollectionSearchTask collectionSearchTask;

    private String searchString;

    private boolean isSearchingOrDisplayingFeaturedCollections;

    private boolean shouldReturnToStreamActivity;

    private EventListener eventListener;

    private ActionButtonClickDelegate
            currentActionButtonClickDelegate;

    private int newCollectionsStartPosition;

    AddCollectionsActivityModel() {
        this.selectedCollectionHolder = new SelectedCollectionHolder() {
            @Override
            void onCollectionListModified() {
                onSelectedCollectionsModified();
            }
        };
        chipViewHeight = R.dimen.height_small;
        chipViewMargin = R.dimen.spacing_xs;
        searchString = "";
        actionButtonAddText = R.string.action_add;
        actionButtonSearchText = R.string.action_search;
        searchCollectionsDelegate = new ActionButtonClickDelegate() {
            @Override
            void handleClick() {
                searchForCollectionsIfNeeded();
            }

            @Override
            int getButtonText() {
                return actionButtonSearchText;
            }
        };
        saveCollectionsDelegate = new ActionButtonClickDelegate() {
            @Override
            void handleClick() {
                saveSelectedCollections();
            }

            @Override
            int getButtonText() {
                return actionButtonAddText;
            }
        };
        setCurrentActionButtonClickDelegate(searchCollectionsDelegate);
    }

    void onInjectionComplete() {
        newCollectionsStartPosition = collectionRepository.size();
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    private void setCurrentActionButtonClickDelegate(
            ActionButtonClickDelegate actionButtonClickDelegate
    ) {
        this.currentActionButtonClickDelegate = actionButtonClickDelegate;
        if (eventListener != null) {
            eventListener.onActionButtonTextChanged(currentActionButtonClickDelegate
                    .getButtonText());
        }
    }

    boolean shouldReturnToStreamActivity() {
        return shouldReturnToStreamActivity;
    }

    void setShouldReturnToStreamActivity(boolean shouldReturnToStreamActivity) {
        this.shouldReturnToStreamActivity = shouldReturnToStreamActivity;
    }

    @DimenRes
    int getChipViewHeight() {
        return chipViewHeight;
    }

    @DimenRes
    int getChipViewMargin() {
        return chipViewMargin;
    }

    void onSearchTextChanged(String newSearchString) {
        searchString = newSearchString != null ? newSearchString : "";
        if (searchString.length() == 0 && !isSearchingOrDisplayingFeaturedCollections) {
            searchForFeaturedCollections();
        }
    }

    private void searchForCollectionsIfNeeded() {
        if (hasSearchTextChangedSinceLastSearch()) {
            cancelSearchTasks();
            collectionSearchTask = collectionSearchTaskFactory.create(
                    searchString,
                    MIN_COLLECTION_SIZE
            );
            collectionSearchTask.setListener(this);
            collectionSearchTask.execute();
            onSearchStarted(false);
        }
        if (eventListener != null) {
            eventListener.onSearchAttempted();
        }
    }

    private void saveSelectedCollections() {
        for (Collection collection : selectedCollectionHolder.getCollectionList()) {
            collectionRepository.insert(collection);
        }
        if (eventListener != null) {
            eventListener.onCollectionsSaved();
        }
    }

    private void searchForFeaturedCollections() {
        cancelSearchTasks();
        featuredCollectionsTask = featuredCollectionsTaskFactory.create();
        featuredCollectionsTask.setListener(this);
        featuredCollectionsTask.execute();
        onSearchStarted(true);
    }

    private void cancelSearchTasks() {
        if (featuredCollectionsTask != null) {
            featuredCollectionsTask.cancel(true);
            featuredCollectionsTask = null;
        }
        if (collectionSearchTask != null) {
            collectionSearchTask.cancel(true);
            collectionSearchTask = null;
        }
    }

    private void onSearchStarted(boolean isFeaturedSearch) {
        isSearchingOrDisplayingFeaturedCollections = isFeaturedSearch;
        selectedCollectionHolder.clear();
        if (eventListener != null) {
            eventListener.onSearchStarted();
        }
    }

    private boolean hasSearchTextChangedSinceLastSearch() {
        String lastSearchString =
                collectionSearchTask != null ? collectionSearchTask.getSearchString() : null;
        return !searchString.equals(lastSearchString);
    }

    void onActionButtonClicked() {
        currentActionButtonClickDelegate.handleClick();
    }

    void onCancelButtonClicked() {
        if (eventListener != null) {
            eventListener.onCancelButtonClicked();
        }
    }

    private void onSelectedCollectionsModified() {
        setCurrentActionButtonClickDelegate(selectedCollectionHolder.size() > 0 ?
                saveCollectionsDelegate : searchCollectionsDelegate);
    }

    void onKeyboardSearchClicked() {
        searchForCollectionsIfNeeded();
    }

    int getSearchProgressText() {
        return isSearchingOrDisplayingFeaturedCollections ?
                R.string.title_loading_suggested_collections :
                R.string.title_searching;
    }

    boolean isSearchingOrDisplayingFeaturedCollections() {
        return isSearchingOrDisplayingFeaturedCollections;
    }

    String getPostSearchStatus(Context context) {
        Resources resources = context.getResources();
        return isSearchingOrDisplayingFeaturedCollections ?
                resources.getString(R.string.title_featured_collections) :
                resources.getString(R.string.title_results_for_collection, searchString);
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList) {
        if (eventListener != null) {
            eventListener.onSearchComplete(collectionList);
        }
    }

    int getNewCollectionsStartPosition() {
        return newCollectionsStartPosition;
    }

    void onActivityReady() {
        searchForFeaturedCollections();
    }

    @Override
    public void onChipViewChecked(Collection checkedCollection) {
        if (!selectedCollectionHolder.contains(checkedCollection)) {
            selectedCollectionHolder.add(checkedCollection);
        }
    }

    @Override
    public void onChipViewUnchecked(Collection uncheckedCollection) {
        selectedCollectionHolder.remove(uncheckedCollection);
    }

    boolean isCollectionSelected(Collection collection) {
        return selectedCollectionHolder.contains(collection);
    }

    int getMinCollectionNameLength() {
        return MIN_COLLECTION_NAME_LENGTH;
    }

    interface EventListener {

        void onActionButtonTextChanged(@StringRes int text);

        void onCancelButtonClicked();

        void onSearchAttempted();

        void onSearchStarted();

        void onSearchComplete(List<Collection> collectionList);

        void onCollectionsSaved();

    }

    private abstract class ActionButtonClickDelegate {

        abstract void handleClick();

        @StringRes
        abstract int getButtonText();

    }

    private abstract class SelectedCollectionHolder {

        private final List<Collection> collectionList;

        private SelectedCollectionHolder() {
            collectionList = new ArrayList<>();
        }

        private void add(Collection collection) {
            collectionList.add(collection);
            onCollectionListModified();
        }

        private void remove(Collection collection) {
            collectionList.remove(collection);
            onCollectionListModified();
        }

        private void clear() {
            collectionList.clear();
            onCollectionListModified();
        }

        private int size() {
            return collectionList.size();
        }

        private List<Collection> getCollectionList() {
            return collectionList;
        }

        abstract void onCollectionListModified();

        boolean contains(Collection collection) {
            return collectionList.contains(collection);
        }
    }

}
