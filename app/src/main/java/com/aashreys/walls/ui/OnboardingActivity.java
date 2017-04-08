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

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTaskFactory;
import com.aashreys.walls.ui.views.ChipView;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class OnboardingActivity extends BaseActivity implements CollectionSearchTask
        .CollectionSearchListener, ChipView.OnCheckedListener {

    private static final String ARG_CHECKED_COLLECTION_LIST = "arg_selected_collection_list";

    @Inject FeaturedCollectionsTaskFactory collectionsTaskFactory;

    @Inject KeyValueStore keyValueStore;

    @Inject CollectionRepository collectionRepository;

    private List<Collection> checkedCollectionList;

    private FeaturedCollectionsTask collectionsTask;

    private ProgressBar progressBar;

    private FlowLayout collectionsParent;

    private Button continueButton;

    private int chipViewHeight, chipViewMargin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getUiComponent().inject(this);
        if (savedInstanceState != null) {
            checkedCollectionList = savedInstanceState.getParcelableArrayList(
                    ARG_CHECKED_COLLECTION_LIST);
        } else {
            checkedCollectionList = new ArrayList<>();
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        collectionsParent = (FlowLayout) findViewById(R.id.parent_collections);
        continueButton = (Button) findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCollectionsAndContinue();
            }
        });

        chipViewHeight = getResources().getDimensionPixelSize(R.dimen.height_small);
        chipViewMargin = getResources().getDimensionPixelOffset(R.dimen.spacing_xs);
        loadCuratedCollections();
        progressBar.setVisibility(View.VISIBLE);
        collectionsParent.setVisibility(View.INVISIBLE);
    }

    private void loadCuratedCollections() {
        if (collectionsTask != null) {
            collectionsTask.cancel(true);
        }
        collectionsTask = collectionsTaskFactory.create();
        collectionsTask.setListener(this);
        collectionsTask.execute();
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList, boolean isCurated) {
        collectionsParent.removeAllViews();
        if (collectionList != null && collectionList.size() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
            collectionsParent.setVisibility(View.VISIBLE);
            for (Collection collection : collectionList) {
                if (collection.getName().value().length() > 3) {
                    ChipView chipView = new ChipView(this);
                    chipView.setCollection(collection);
                    chipView.setOnCheckedListener(this);
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            chipViewHeight
                    );
                    params.setMargins(
                            chipViewMargin,
                            chipViewMargin,
                            chipViewMargin,
                            chipViewMargin
                    );
                    collectionsParent.addView(chipView, params);
                    chipView.setChecked(checkedCollectionList.contains(collection));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(
                ARG_CHECKED_COLLECTION_LIST,
                (ArrayList<? extends Parcelable>) checkedCollectionList
        );
    }

    @Override
    public void onChipViewChecked(
            ChipView checkedChipView, Collection checkedCollection
    ) {
        if (checkedCollection != null) {
            checkedCollectionList.add(checkedCollection);
        }
    }

    @Override
    public void onChipViewUnchecked(
            ChipView uncheckedChipView, Collection uncheckedCollection
    ) {
        if (uncheckedCollection != null) {
            checkedCollectionList.remove(uncheckedCollection);
        }
    }

    private void saveCollectionsAndContinue() {
        for (Collection collection : checkedCollectionList) {
            collectionRepository.insert(collection);
        }
        keyValueStore.putBoolean(StreamActivityViewModel.KEY_IS_ONBOARDING_COMPLETED, true);
        finish();
        startActivity(new Intent(this, StreamActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
