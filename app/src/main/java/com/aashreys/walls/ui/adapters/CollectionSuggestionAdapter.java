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
        implements ChipView.OnCheckedListener {

    private final List<Collection> collectionList;

    @Nullable
    private ChipView selectedChipView;

    @Nullable
    private Collection selectedCollection;

    private boolean isMultiSelectEnabled;

    @Nullable
    private ChipView.OnCheckedListener chipViewSelectedListener;

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
    public void onChipViewChecked(
            ChipView checkedChipView, Collection checkedCollection
    ) {
        if (!isMultiSelectEnabled) {
            if (selectedChipView != null) {
                selectedChipView.setChecked(false);
            }
            selectedChipView = checkedChipView;
            selectedCollection = checkedCollection;

        }
        if (chipViewSelectedListener != null) {
            chipViewSelectedListener.onChipViewChecked(checkedChipView, checkedCollection);
        }
    }

    @Override
    public void onChipViewUnchecked(
            ChipView uncheckedChipView, Collection uncheckedCollection
    ) {
        if (chipViewSelectedListener != null) {
            chipViewSelectedListener.onChipViewUnchecked(uncheckedChipView, uncheckedCollection);
        }
    }

    public void setOnChipViewListener(@Nullable ChipView.OnCheckedListener listener) {
        this.chipViewSelectedListener = listener;
    }

    public void resetState() {
        if (selectedChipView != null) {
            selectedChipView.resetState();
        }
        selectedCollection = null;
        setCollections(null);
    }

    public void setMultiSelectEnabled(boolean isEnabled) {
        this.isMultiSelectEnabled = isEnabled;
    }

    static class CollectionSuggestionViewHolder extends RecyclerView.ViewHolder {

        private ChipView chipView;

        private CollectionSuggestionViewHolder(ChipView itemView) {
            super(itemView);
            this.chipView = itemView;
        }

        private void bind(
                Collection collection, @Nullable Collection selectedCollection,
                ChipView.OnCheckedListener listener
        ) {
            chipView.setCollection(collection);
            if (selectedCollection != null) {
                chipView.setChecked(selectedCollection.getId().equals(collection.getId()));
            } else {
                chipView.setChecked(false);
            }
            chipView.setOnCheckedListener(listener);
        }
    }

}
