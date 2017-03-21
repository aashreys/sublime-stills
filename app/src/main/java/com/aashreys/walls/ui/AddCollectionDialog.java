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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.adapters.CollectionSuggestionAdapter;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.CollectionSearchTaskFactory;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTask;
import com.aashreys.walls.ui.tasks.FeaturedCollectionsTaskFactory;
import com.aashreys.walls.ui.views.ChipView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 01/02/17.
 */

public class AddCollectionDialog extends DialogFragment implements
        ChipView.OnCheckedListener, CollectionSearchTask.CollectionSearchListener {

    private static final String TAG = AddCollectionDialog.class.getSimpleName();

    @Inject CollectionRepository collectionRepository;

    @Inject CollectionSearchTaskFactory collectionSearchTaskFactory;

    @Inject FeaturedCollectionsTaskFactory featuredCollectionsTaskFactory;

    private FeaturedCollectionsTask featuredCollectionsTask;

    private CollectionSearchTask collectionSearchTask;

    private ViewGroup collectionsParent;

    private EditText collectionInput;

    private RecyclerView collectionsList;

    private ViewGroup searchProgressParent;

    private TextView searchStatusText;

    private Button dialogButton;

    private Collection selectedCollection;

    private CollectionSuggestionAdapter adapter;

    public static void showNewInstance(BaseActivity activity, String tag) {
        AddCollectionDialog fragment = new AddCollectionDialog();
        fragment.show(activity.getSupportFragmentManager(), tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_add_collection,
                null,
                false
        );
        collectionInput = (EditText) contentView.findViewById(R.id.input_collection);
        collectionInput.setHint(R.string.hint_collection_search);
        collectionsParent = (ViewGroup) contentView.findViewById(R.id.parent_collections);
        collectionsList = (RecyclerView) contentView.findViewById(R.id.list_collections);
        searchProgressParent = (ViewGroup) contentView.findViewById(R.id.parent_search_progress);
        searchStatusText = (TextView) contentView.findViewById(R.id.text_search_status);

        adapter = new CollectionSuggestionAdapter();
        adapter.setOnChipViewListener(this);
        collectionsList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        collectionsList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(
                    Rect outRect, View view, RecyclerView parent, RecyclerView.State state
            ) {
                int margin = getResources().getDimensionPixelOffset(R.dimen.spacing_xxs);
                outRect.left = margin;
                outRect.right = margin;
            }
        });
        collectionsList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_add_collection_dialog);
        builder.setView(contentView);
        builder.setPositiveButton(R.string.action_search, null);
        builder.setNegativeButton(R.string.action_cancel, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialogButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                dialogButton.setEnabled(false);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
        collectionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetState();
            }

            @Override
            public void afterTextChanged(Editable s) {
                setSelectedCollection(null);
                if (s.length() == 0) {
                    loadFeaturedCollections();
                }
            }
        });
        collectionInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                dialogButton.callOnClick();
                return true;
            }
        });
        loadFeaturedCollections();
        return dialog;
    }

    private void insertCollection(Collection collection) {
        collectionRepository.insert(collection);
    }

    private void searchForCollections(String searchString) {
        if (featuredCollectionsTask != null) {
            featuredCollectionsTask.cancel(true);
        }
        if (collectionSearchTask != null) {
            collectionSearchTask.cancel(true);
        }
        collectionSearchTask = collectionSearchTaskFactory.create();
        collectionSearchTask.setListener(this);
        collectionSearchTask.execute(searchString);
        showSearchProgress(R.string.title_searching);
    }

    private void loadFeaturedCollections() {
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
    }

    private void resetState() {
        collectionsParent.setVisibility(View.GONE);
        collectionsList.setVisibility(View.GONE);
        searchProgressParent.setVisibility(View.GONE);
        adapter.resetState();
        dialogButton.setText(R.string.action_search);
        collectionsList.scrollToPosition(0);
    }

    private void onCollectionsFound(List<Collection> collections) {
        adapter.setCollections(collections);
        collectionsList.scrollToPosition(0);
    }

    private void showSearchProgress(@StringRes int searchStatus) {
        collectionsParent.setVisibility(View.VISIBLE);
        searchStatusText.setText(searchStatus);
        searchProgressParent.setVisibility(View.VISIBLE);
        collectionsList.setVisibility(View.GONE);
    }

    private void showCollectionsList() {
        collectionsParent.setVisibility(View.VISIBLE);
        searchProgressParent.setVisibility(View.GONE);
        collectionsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChipViewChecked(
            @Nullable ChipView checkedChipView, @Nullable Collection checkedCollection
    ) {
        setSelectedCollection(checkedCollection);
    }

    @Override
    public void onChipViewUnchecked(
            ChipView uncheckedChipView, Collection uncheckedCollection
    ) {
        // Do nothing
    }

    private void setSelectedCollection(final Collection collection) {
        this.selectedCollection = collection;
        if (dialogButton != null) {
            dialogButton.setEnabled(true);
            if (collection != null) {
                dialogButton.setEnabled(true);
                dialogButton.setText(R.string.action_add);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionRepository.insert(collection);
                        dismiss();
                    }
                });
            } else {
                dialogButton.setEnabled(collectionInput.getText().length() > 0);
                dialogButton.setText(R.string.action_search);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchForCollections(collectionInput.getText().toString());
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.setOnChipViewListener(null);
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList, boolean isFeatured) {
        if (collectionList.size() > 0) {
            if (isFeatured) {
                Collections.shuffle(collectionList);
            }
            showCollectionsList();
            onCollectionsFound(collectionList);
        } else {
            resetState();
        }
    }
}
