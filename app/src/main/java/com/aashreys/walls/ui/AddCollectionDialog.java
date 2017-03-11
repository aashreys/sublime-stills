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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.adapters.CollectionSuggestionAdapter;
import com.aashreys.walls.ui.tasks.CollectionSearchTask;
import com.aashreys.walls.ui.tasks.CollectionSearchTaskFactory;
import com.aashreys.walls.ui.views.ChipView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 01/02/17.
 */

public class AddCollectionDialog extends DialogFragment implements
        ChipView.OnSelectedListener, CollectionSearchTask.CollectionSearchListener {

    private static final String TAG = AddCollectionDialog.class.getSimpleName();

    @Inject CollectionRepository collectionRepository;

    @Inject CollectionSearchTaskFactory collectionSearchTaskFactory;

    private CollectionSearchTask collectionSearchTask;

    private ViewGroup collectionsParent;

    private EditText collectionInput;

    private RecyclerView collectionsList;

    private ProgressBar progressBar;

    private Button dialogButton;

    private CollectionSuggestionAdapter adapter;

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
        collectionsParent = (ViewGroup) contentView.findViewById(R.id.parent_collections);
        collectionsList = (RecyclerView) contentView.findViewById(R.id.list_collections);
        progressBar = (ProgressBar) contentView.findViewById(R.id.progress_bar);
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
                int margin = getResources().getDimensionPixelOffset(R.dimen.chip_margin);
                outRect.left = margin;
                outRect.right = margin;
            }
        });
        collectionsList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_search_and_add_collection);
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
                        if (adapter.getSelectedCollection() != null) {
                            insertCollection(adapter.getSelectedCollection());
                            dialog.dismiss();
                        } else if (!collectionInput.getText().toString().isEmpty()) {
                            searchForCollections(collectionInput.getText().toString());
                        }
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
                dialogButton.setEnabled(!s.toString().isEmpty());
            }
        });
        collectionInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                dialogButton.callOnClick();
                return true;
            }
        });
        return dialog;
    }

    private void insertCollection(Collection collection) {
        collectionRepository.insert(collection);
    }

    private void searchForCollections(String searchString) {
        if (collectionSearchTask != null) {
            collectionSearchTask.cancel(true);
        }
        collectionSearchTask = collectionSearchTaskFactory.create();
        collectionSearchTask.setListener(this);
        collectionSearchTask.execute(searchString);
        showProgressBar();
    }

    private void resetState() {
        collectionsParent.setVisibility(View.GONE);
        collectionsList.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        adapter.resetState();
        dialogButton.setText(R.string.action_search);
        collectionsList.scrollToPosition(0);
    }

    private void onCollectionsFound(List<Collection> collections) {
        adapter.setCollections(collections);
        collectionsList.scrollToPosition(0);
    }

    private void showProgressBar() {
        collectionsParent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        collectionsList.setVisibility(View.GONE);
    }

    private void showCollectionsList() {
        collectionsParent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        collectionsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChipViewSelected(
            @Nullable ChipView view, @Nullable Collection collection
    ) {
        if (dialogButton != null) {
            dialogButton.setText(collection != null ? R.string.action_add : R.string.action_search);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.setOnChipViewListener(null);
    }

    @Override
    public void onSearchComplete(List<Collection> collections) {
        showCollectionsList();
        onCollectionsFound(collections);
    }
}
