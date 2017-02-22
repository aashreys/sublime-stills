package com.aashreys.walls.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.ui.views.ChipView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aashreys on 05/02/17.
 */

public class CollectionSuggestionAdapter extends
        RecyclerView.Adapter<CollectionSuggestionAdapter.CollectionSuggestionViewHolder>
        implements ChipView.OnSelectedListener {

    private final List<Collection> collectionList;

    @Nullable
    private ChipView selectedChipView;

    @Nullable
    private Collection selectedCollection;

    @Nullable
    private ChipView.OnSelectedListener chipViewSelectedListener;

    public CollectionSuggestionAdapter() {
        this.collectionList = new ArrayList<>();
    }

    public void setCollections(@Nullable List<Collection> collections) {
        this.collectionList.clear();
        if (collections != null) {
            this.collectionList.addAll(collections);
        }
        notifyDataSetChanged();
    }

    @Override
    public CollectionSuggestionViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType
    ) {
        return new CollectionSuggestionViewHolder(new ChipView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(
            CollectionSuggestionViewHolder holder,
            int position
    ) {
        holder.bind(collectionList.get(position), selectedCollection, this);
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    @Override
    public void onChipViewSelected(
            ChipView view, Collection collection
    ) {
        if (selectedChipView != null) {
            selectedChipView.setChecked(false);
        }
        selectedChipView = view;
        selectedCollection = collection;
        if (chipViewSelectedListener != null) {
            chipViewSelectedListener.onChipViewSelected(view, collection);
        }
    }

    public void setOnChipViewListener(@Nullable ChipView.OnSelectedListener listener) {
        this.chipViewSelectedListener = listener;
    }

    @Nullable
    public Collection getSelectedCollection() {
        return selectedCollection;
    }

    public void resetState() {
        if (selectedChipView != null) {
            selectedChipView.resetState();
        }
        selectedCollection = null;
        setCollections(null);
    }

    static class CollectionSuggestionViewHolder extends RecyclerView.ViewHolder {

        private ChipView chipView;

        private CollectionSuggestionViewHolder(ChipView itemView) {
            super(itemView);
            this.chipView = itemView;
        }

        private void bind(
                Collection collection, @Nullable Collection selectedCollection,
                ChipView.OnSelectedListener listener
        ) {
            chipView.setCollection(collection);
            if (selectedCollection != null) {
                chipView.setChecked(selectedCollection.id().equals(collection.id()));
            } else {
                chipView.setChecked(false);
            }
            chipView.setOnSelectedListener(listener);
        }
    }

}
