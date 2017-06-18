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
import com.aashreys.walls.application.views.ChipView;
import com.aashreys.walls.domain.display.collections.Collection;
import com.wefika.flowlayout.FlowLayout;

import java.util.Collections;
import java.util.List;

public class AddCollectionsActivity extends BaseActivity<AddCollectionsActivityModel> implements
        AddCollectionsActivityModel.EventListener {

    private static final String ARG_RETURN_TO_STREAM = "arg_return_to_stream";

    private EditText collectionInput;

    private TextView searchStatusText;

    private FlowLayout collectionsParent;

    private Button actionButton, cancelButton;

    private ProgressBar progressBar;

    public static final Intent createLaunchIntent(
            Context context,
            boolean isReturnToStreamActivity
    ) {
        Intent intent = new Intent(context, AddCollectionsActivity.class);
        intent.putExtra(ARG_RETURN_TO_STREAM, isReturnToStreamActivity);
        return intent;
    }

    @Override
    protected AddCollectionsActivityModel createViewModel() {
        return getUiComponent().createAddCollectionsActivityModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collections);
        getViewModel().setEventListener(this);
        getViewModel().setShouldReturnToStreamActivity(getIntent().getBooleanExtra(
                ARG_RETURN_TO_STREAM,
                false
        ));

        collectionsParent = (FlowLayout) findViewById(R.id.parent_collections);
        searchStatusText = (TextView) findViewById(R.id.text_search_status);
        collectionInput = (EditText) findViewById(R.id.input_collection);
        actionButton = (Button) findViewById(R.id.button_action);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        collectionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getViewModel().onSearchTextChanged(s.toString());
            }
        });
        collectionInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getViewModel().onKeyboardSearchClicked();
                return true;
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().onActionButtonClicked();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().onCancelButtonClicked();
                finish();
            }
        });
        getViewModel().onActivityReady();
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public void onSearchAttempted() {
        hideKeyboard();
    }

    @Override
    public void onSearchStarted() {
        collectionsParent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        searchStatusText.setText(getViewModel().getSearchProgressText());
    }

    @Override
    public void onActionButtonTextChanged(@StringRes int text) {
        actionButton.setText(text);
    }

    @Override
    public void onCancelButtonClicked() {
        finish();
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList) {
        collectionsParent.removeAllViews();
        progressBar.setVisibility(View.GONE);
        searchStatusText.setText(getViewModel().getPostSearchStatus(this));
        collectionsParent.setVisibility(View.VISIBLE);
        if (collectionList != null && collectionList.size() > 0) {
            if (getViewModel().isSearchingOrDisplayingFeaturedCollections()) {
                Collections.shuffle(collectionList);
            }
            for (Collection collection : collectionList) {
                ChipView chipView = new ChipView(this);
                chipView.setCollection(collection);
                chipView.setChecked(getViewModel().isCollectionSelected(collection));
                chipView.setOnCheckedListener(getViewModel());
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        getResources().getDimensionPixelSize(getViewModel().getChipViewHeight())
                );
                int margin = getResources()
                        .getDimensionPixelSize(getViewModel().getChipViewMargin());
                params.setMargins(margin, margin, margin, margin);
                collectionsParent.addView(chipView, params);
            }
        }
    }

    @Override
    public void onCollectionsSaved() {
        if (getViewModel().shouldReturnToStreamActivity()) {
            startActivity(StreamActivity.createLaunchIntent(
                    this,
                    getViewModel().getNewCollectionsStartPosition()
            ));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        getViewModel().onActivityDestroyed();
        super.onDestroy();
    }
}
