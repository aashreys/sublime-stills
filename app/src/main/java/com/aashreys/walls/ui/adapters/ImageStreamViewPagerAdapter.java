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
public class ImageStreamViewPagerAdapter extends FragmentStatePagerAdapter implements
        ImageStreamFragment.CollectionProvider {

    private final CollectionRepository collectionRepository;

    private final List<Collection> collectionList;

    private final SparseArray<ImageStreamFragment> positionFragmentArray;

    private CollectionAddedListener collectionAddedListener;

    private final CollectionRepositoryListener repositoryCallback
            = new CollectionRepositoryListener() {
        @Override
        public void onReplaceAll(List<Collection> collections) {
            collectionList.clear();
            collectionList.addAll(collections);
            notifyDataSetChanged();
        }

        @Override
        public void onInsert(Collection object) {
            collectionList.add(object);
            notifyDataSetChanged();
            if (collectionAddedListener != null) {
                collectionAddedListener.onCollectionAdded(object, collectionList.size() - 1);
            }
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
        return ImageStreamFragment.newInstance(position);
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
            return index >= 0 ? positionFragmentArray.keyAt(index) : POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < positionFragmentArray.size(); i++) {
            ImageStreamFragment fragment = positionFragmentArray.valueAt(i);
            int position = positionFragmentArray.keyAt(i);
            if (position < collectionList.size()) {
                fragment.setCollection(collectionList.get(position));
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return collectionList.get(position).getName().value();
    }

    public void release() {
        collectionRepository.removeListener(repositoryCallback);
    }

    @Override
    public Collection getCollection(int position) {
        return collectionList.get(position);
    }

    public void setCollectionAddedListener(CollectionAddedListener collectionAddedListener) {
        this.collectionAddedListener = collectionAddedListener;
    }

    public interface CollectionAddedListener {

        void onCollectionAdded(Collection collection, int position);

    }
}
