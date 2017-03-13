package com.aashreys.walls.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.adapters.CollectionsAdapter;
import com.aashreys.walls.ui.views.CollectionView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 21/02/17.
 */

public class CollectionsActivity extends BaseActivity implements CollectionsAdapter
        .OnStartDragListener {

    private static final String TAG = CollectionsActivity.class.getSimpleName();

    private static final String ADD_COLLECTION_FRAGMENT_TAG = "tag_add_collection_fragment";

    @Inject CollectionRepository collectionRepository;

    private CollectionsAdapter adapter;

    private CollectionRepository.CollectionRepositoryListener repositoryCallback;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        getUiComponent().inject(this);
        this.repositoryCallback = new CollectionRepository.CollectionRepositoryListener() {
            @Override
            public void onReplace(List<Collection> collections) {

            }

            @Override
            public void onInsert(Collection object) {
                adapter.add(object);
            }

            @Override
            public void onUpdate(Collection object) {

            }

            @Override
            public void onDelete(Collection object) {
                adapter.remove(object);
            }
        };
        RecyclerView collectionRecyclerView = (RecyclerView) findViewById(R.id.list_image_sources);
        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CollectionsAdapter();
        adapter.setOnCollectionClickListener(new CollectionsAdapter.OnCollectionClickListener() {
            @Override
            public void onClick(CollectionView view, int position) {
                saveCollections();
                finish();
                Context context = view.getContext();
                context.startActivity(StreamActivity.createLaunchIntent(
                        view.getContext(),
                        position
                ));
            }
        });
        adapter.setDragListener(this);
        collectionRecyclerView.setAdapter(adapter);
        adapter.add(collectionRepository.getAll());
        collectionRepository.addListener(repositoryCallback);

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
                AddCollectionDialog fragment
                        = new AddCollectionDialog();
                fragment.show(getSupportFragmentManager(), ADD_COLLECTION_FRAGMENT_TAG);
            }
        });
    }

    private void saveCollections() {
        if (adapter.isCollectionListModified()) {
            collectionRepository.replace(adapter.getCollectionList());
        }
        adapter.onCollectionsSaved();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        collectionRepository.removeListener(repositoryCallback);
        if (adapter.isCollectionListModified()) {
            collectionRepository.replace(adapter.getCollectionList());
        }
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }
}
