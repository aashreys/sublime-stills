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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.CollectionSearchTaskFactory;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTaskFactory;
import com.aashreys.walls.ui.views.ChipView;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class AddCollectionsActivity extends BaseActivity implements ChipView.OnCheckedListener,
        CollectionSearchTask.CollectionSearchListener {

    private static final String ARG_RETURN_TO_STREAM = "arg_return_to_stream";

    @Inject FeaturedCollectionsTaskFactory featuredCollectionsTaskFactory;

    @Inject CollectionSearchTaskFactory collectionSearchTaskFactory;

    @Inject CollectionRepository collectionRepository;

    private FeaturedCollectionsTask featuredCollectionsTask;

    private CollectionSearchTask collectionSearchTask;

    private List<Collection> checkedCollectionList;

    private EditText collectionInput;

    private TextView searchStatusText;

    private FlowLayout collectionsParent;

    private Button actionButton, cancelButton;

    private ProgressBar progressBar;

    private int chipViewHeight, chipViewMargin;

    private String postSearchStatusText;

    private boolean isLoadingOrDisplayingFeatured;

    private boolean isReturnToStreamActivity;

    private int minPhotos;

    public static final Intent createLaunchIntent(Context context, boolean isReturnToStreamActivity) {
        Intent intent =  new Intent(context, AddCollectionsActivity.class);
        intent.putExtra(ARG_RETURN_TO_STREAM, isReturnToStreamActivity);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collections);
        getUiComponent().inject(this);
        isReturnToStreamActivity = getIntent().getBooleanExtra(ARG_RETURN_TO_STREAM, false);
        checkedCollectionList = new ArrayList<>();
        collectionsParent = (FlowLayout) findViewById(R.id.parent_collections);
        searchStatusText = (TextView) findViewById(R.id.text_search_status);
        collectionInput = (EditText) findViewById(R.id.input_collection);
        actionButton = (Button) findViewById(R.id.button_action);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        chipViewHeight = getResources().getDimensionPixelSize(R.dimen.height_small);
        chipViewMargin = getResources().getDimensionPixelOffset(R.dimen.spacing_xs);
        minPhotos = getResources().getInteger(R.integer.min_photos_in_collection);
        collectionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && !isLoadingOrDisplayingFeatured) {
                    searchFeaturedCollections();
                }
            }
        });
        collectionInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchForCollections(collectionInput.getText().toString());
                return true;
            }
        });
        searchFeaturedCollections();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForCollections(collectionInput.getText().toString());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList, boolean isCurated) {
        isLoadingOrDisplayingFeatured = isCurated;
        collectionsParent.removeAllViews();
        searchStatusText.setText(postSearchStatusText);
        progressBar.setVisibility(View.GONE);
        collectionsParent.setVisibility(View.VISIBLE);
        if (collectionList != null && collectionList.size() > 0) {
            if (isCurated) {
                Collections.shuffle(collectionList);
            }
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
    public void onChipViewChecked(Collection checkedCollection) {
        if (checkedCollection != null) {
            checkedCollectionList.add(checkedCollection);
            onCheckedCollectionsModified();
        }
    }

    @Override
    public void onChipViewUnchecked(Collection uncheckedCollection) {
        if (uncheckedCollection != null) {
            checkedCollectionList.remove(uncheckedCollection);
            onCheckedCollectionsModified();
        }
    }

    private void onCheckedCollectionsModified() {
        int size = checkedCollectionList.size();
        if (size > 0) {
            actionButton.setText(R.string.action_add);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveCollectionsAndFinish();
                }
            });
        } else {
            actionButton.setText(R.string.action_search);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchForCollections(collectionInput.getText().toString());
                }
            });
        }
    }

    private void saveCollectionsAndFinish() {
        int oldSize = collectionRepository.size();
        for (Collection collection : checkedCollectionList) {
            collectionRepository.insert(collection);
        }
        if (isReturnToStreamActivity) {
            startActivity(StreamActivity.createLaunchIntent(this, oldSize));
        }
        finish();
    }

    private void searchFeaturedCollections() {
        beforeSearchStarted(true);
        if (featuredCollectionsTask != null) {
            featuredCollectionsTask.cancel(true);
        }
        if (collectionSearchTask != null) {
            collectionSearchTask.cancel(true);
        }
        featuredCollectionsTask = featuredCollectionsTaskFactory.create();
        featuredCollectionsTask.setListener(this);
        featuredCollectionsTask.execute();
        showSearchProgress(R.string.title_loading_suggested_collections);
        postSearchStatusText = getString(R.string.title_featured_collections);
        isLoadingOrDisplayingFeatured = true;
    }

    private void searchForCollections(String searchString) {
        beforeSearchStarted(false);
        if (featuredCollectionsTask != null) {
            featuredCollectionsTask.cancel(true);
        }
        if (collectionSearchTask != null) {
            collectionSearchTask.cancel(true);
        }
        collectionSearchTask = collectionSearchTaskFactory.create(minPhotos);
        collectionSearchTask.setListener(this);
        collectionSearchTask.execute(searchString);
        showSearchProgress(R.string.title_searching);
        postSearchStatusText = getString(R.string.title_results_for_collection, searchString);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void showSearchProgress(@StringRes int loadingMessage) {
        collectionsParent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        searchStatusText.setText(loadingMessage);
    }

    private void beforeSearchStarted(boolean isFeatured) {
        if (!isFeatured) {
            hideKeyboard();
        }
        clearCheckedCollections();
    }

    private void clearCheckedCollections() {
        checkedCollectionList.clear();
        actionButton.setText(R.string.action_search);
        onCheckedCollectionsModified();
    }
}
