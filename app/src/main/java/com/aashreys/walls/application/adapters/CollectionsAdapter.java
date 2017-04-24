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

package com.aashreys.walls.application.adapters;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.application.helpers.UiHelper;
import com.aashreys.walls.application.views.CollectionView;

/**
 * Created by aashreys on 04/02/17.
 */

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter
        .CollectionViewHolder> implements CollectionsAdapterModel.EventListener {

    private static final String TAG = CollectionsAdapter.class.getSimpleName();

    private DragListener dragListener;

    private OnCollectionClickListener onCollectionClickListener;

    private CollectionsAdapterModel adapterModel;

    public CollectionsAdapter(CollectionsAdapterModel adapterModel) {
        this.adapterModel = adapterModel;
        this.adapterModel.setEventListener(this);
    }

    @Override
    public void onCollectionAdded(int position) {
        notifyItemInserted(position);
    }

    @Override
    public void onCollectionRemoved(int position) {
        notifyItemRemoved(position);
    }

    @Override
    public void onCollectionListChanged() {
        notifyDataSetChanged();
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        return new CollectionViewHolder((CollectionView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection, parent, false));
    }

    @Override
    public void onBindViewHolder(final CollectionViewHolder holder, int position) {
        holder.bind(adapterModel.getCollection(position), new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onDragStarted(holder);
                }
                return true;
            }
        }, onCollectionClickListener);
    }

    @Override
    public int getItemCount() {
        return adapterModel.collectionListSize();
    }

    private void onItemMove(int fromPosition, int toPosition) {
        adapterModel.moveCollection(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void onDragStarted(RecyclerView.ViewHolder holder) {
        dragListener.onDragStarted(holder);
    }

    private void onDragFinished() {
        dragListener.onDragFinished();
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public void setOnCollectionClickListener(OnCollectionClickListener onCollectionClickListener) {
        this.onCollectionClickListener = onCollectionClickListener;
    }

    public void release() {
        adapterModel.setEventListener(null);
    }

    public interface DragListener {

        void onDragStarted(RecyclerView.ViewHolder holder);

        void onDragFinished();

    }

    public interface OnCollectionClickListener {

        void onClick(CollectionView view, int position);

    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {

        private CollectionView collectionView;

        public CollectionViewHolder(CollectionView itemView) {
            super(itemView);
            this.collectionView = itemView;
        }

        private void bind(
                Collection collection,
                View.OnTouchListener dragHandlerOnTouchListener,
                final OnCollectionClickListener onClickListener
        ) {
            this.collectionView.setCollection(collection);
            this.collectionView.setDragHandleOnTouchListener(dragHandlerOnTouchListener);
            this.collectionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick((CollectionView) v, getAdapterPosition());
                }
            });
        }

        private void onSelectedForDrag() {
            collectionView.setBackgroundColor(UiHelper.getColor(
                    collectionView.getContext(),
                    R.color.windowBackground
            ));
        }

        private void onDeselected() {
            TypedValue outValue = new TypedValue();
            collectionView.getContext()
                    .getTheme()
                    .resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            collectionView.setBackgroundResource(outValue.resourceId);
        }
    }

    public static class ItemMoveHelperCallback extends ItemTouchHelper.Callback {

        private final CollectionsAdapter adapter;

        public ItemMoveHelperCallback(CollectionsAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(
                RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
        ) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(
                RecyclerView recyclerView,
                RecyclerView.ViewHolder source,
                RecyclerView.ViewHolder target
        ) {
            adapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        private void dragFinished() {
            adapter.onDragFinished();
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ((CollectionViewHolder) viewHolder).onSelectedForDrag();
            }
            super.onSelectedChanged(viewHolder, actionState);

        }

        @Override
        public void clearView(
                RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
        ) {
            super.clearView(recyclerView, viewHolder);
            ((CollectionViewHolder) viewHolder).onDeselected();
            dragFinished();
        }
    }

}
