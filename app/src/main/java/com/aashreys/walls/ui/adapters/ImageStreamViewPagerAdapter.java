package com.aashreys.walls.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.persistence.collections.CollectionRepository.CollectionRepositoryListener;
import com.aashreys.walls.ui.ImageStreamFragment;

import java.util.List;

/**
 * Created by aashreys on 05/02/17.
 */
public class ImageStreamViewPagerAdapter extends FragmentStatePagerAdapter {

    private final CollectionRepository collectionRepository;

    private final List<Collection> collectionList;

    private final SparseArray<ImageStreamFragment> positionFragmentArray;

    private final CollectionRepositoryListener repositoryCallback
            = new CollectionRepositoryListener() {
        @Override
        public void onReplace(List<Collection> collections) {
            collectionList.clear();
            collectionList.addAll(collections);
            notifyDataSetChanged();
        }

        @Override
        public void onInsert(Collection object) {
            collectionList.add(object);
            notifyDataSetChanged();
        }

        @Override
        public void onUpdate(Collection object) {

        }

        @Override
        public void onDelete(Collection object) {
            collectionList.remove(object);
            notifyDataSetChanged();
        }
    };

    public ImageStreamViewPagerAdapter(
            FragmentManager fm,
            CollectionRepository collectionRepository
    ) {
        super(fm);
        this.positionFragmentArray = new SparseArray<>();
        this.collectionRepository = collectionRepository;
        this.collectionList = collectionRepository.getAll();
        collectionRepository.addListener(repositoryCallback);
    }

    @Override
    public Fragment getItem(int position) {
        return ImageStreamFragment.newInstance(collectionList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageStreamFragment fragment = (ImageStreamFragment)
                super.instantiateItem(container, position);
        positionFragmentArray.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        positionFragmentArray.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof ImageStreamFragment) {
            int index = positionFragmentArray.indexOfValue((ImageStreamFragment) object);
            int position = index >= 0 ? positionFragmentArray.keyAt(index) : POSITION_NONE;
            return position;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < positionFragmentArray.size(); i++) {
            ImageStreamFragment fragment = positionFragmentArray.valueAt(i);
            int position = positionFragmentArray.keyAt(i);
            if (position < collectionList.size()) {
                fragment.setCollection(collectionList.get(position), true);
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return collectionList.get(position).name().value();
    }

    public void release() {
        collectionRepository.removeListener(repositoryCallback);
    }
}
