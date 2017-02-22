package com.aashreys.walls.ui.adapters;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.ui.views.CollectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aashreys on 04/02/17.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter
        .CollectionViewHolder> {

    private static final String TAG = CollectionAdapter.class.getSimpleName();

    private final List<Collection> collectionList;

    private boolean isMoved;

    private OnStartDragListener dragListener;

    public CollectionAdapter() {
        this.collectionList = new ArrayList<>();
    }

    public void add(Collection collection) {
        collectionList.add(collection);
        notifyItemInserted(collectionList.size() - 1);
    }

    public void remove(Collection collection) {
        int position = collectionList.indexOf(collection);
        if (position != -1) {
            collectionList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(List<Collection> collections) {
        collectionList.addAll(collections);
        notifyItemRangeInserted(collectionList.size() - collections.size(), collections.size());
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
        holder.bind(collectionList.get(position), new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDrag(holder);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    private void onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, "onItemMove from " + fromPosition + " to " + toPosition);
        Collection collection = collectionList.remove(fromPosition);
        collectionList.add(toPosition, collection);
        notifyItemMoved(fromPosition, toPosition);
        isMoved = true;
    }

    private void onStartDrag(RecyclerView.ViewHolder holder) {
        dragListener.onDragStarted(holder);
    }

    public boolean isMoved() {
        return isMoved;
    }

    public List<Collection> getCollectionList() {
        return collectionList;
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {

        private CollectionView collectionView;

        public CollectionViewHolder(CollectionView itemView) {
            super(itemView);
            this.collectionView = itemView;
        }

        private void bind(Collection collection, View.OnTouchListener listener) {
            this.collectionView.setCollection(collection);
            this.collectionView.setHandleOnTouchListener(listener);
        }

        private void onSelected() {

        }
    }

    public static class ItemMoveHelperCallback extends ItemTouchHelper.Callback {

        private final CollectionAdapter adapter;

        public ItemMoveHelperCallback(CollectionAdapter adapter) {
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

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
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
                ((CollectionViewHolder) viewHolder).onSelected();
            }
            super.onSelectedChanged(viewHolder, actionState);

        }
    }

    public void setDragListener(OnStartDragListener dragListener) {
        this.dragListener = dragListener;
    }

    public interface OnStartDragListener {

        void onDragStarted(RecyclerView.ViewHolder holder);

    }

}
