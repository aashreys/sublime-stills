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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.ui.adapters.CollectionsAdapter;
import com.aashreys.walls.ui.views.CollectionView;

/**
 * Created by aashreys on 21/02/17.
 */

public class CollectionsActivity extends BaseActivity<CollectionsActivityModel> implements
        CollectionsAdapter.DragListener, CollectionsActivityModel.EventListener {

    private static final String TAG = CollectionsActivity.class.getSimpleName();

    private CollectionsAdapter adapter;

    private ItemTouchHelper itemTouchHelper;

    private Snackbar dragHintSnackbar;

    private RecyclerView collectionRecyclerView;

    @Override
    protected CollectionsActivityModel createViewModel() {
        CollectionsActivityModel viewModel = new CollectionsActivityModel();
        getUiComponent().inject(viewModel);
        viewModel.onInjectionComplete();
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        getViewModel().setEventListener(this);
        collectionRecyclerView = (RecyclerView) findViewById(R.id.list_image_sources);
        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CollectionsAdapter(getViewModel().getCollectionsAdapterModel());
        adapter.setOnCollectionClickListener(new CollectionsAdapter.OnCollectionClickListener() {
            @Override
            public void onClick(CollectionView view, int position) {
                getViewModel().onCollectionClicked(position);
            }
        });
        adapter.setDragListener(this);
        collectionRecyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new CollectionsAdapter.ItemMoveHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(collectionRecyclerView);

        ImageButton closeButton = (ImageButton) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton addCollectionButton = (ImageButton) findViewById(R.id.button_add_collection);
        addCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddCollectionsActivity.createLaunchIntent(
                        CollectionsActivity.this,
                        false
                ));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.release();
        getViewModel().onActivityDestroyed();
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder holder) {
        itemTouchHelper.startDrag(holder);
        dragHintSnackbar = Snackbar.make(
                collectionRecyclerView,
                R.string.hint_reorder_collection,
                BaseTransientBottomBar.LENGTH_INDEFINITE
        );
        dragHintSnackbar.show();
    }

    @Override
    public void onDragFinished() {
        if (dragHintSnackbar != null) {
            dragHintSnackbar.dismiss();
        }
    }

    @Override
    public void onCollectionClicked(int position) {
        startActivity(StreamActivity.createLaunchIntent(this, position));
        finish();
    }
}
