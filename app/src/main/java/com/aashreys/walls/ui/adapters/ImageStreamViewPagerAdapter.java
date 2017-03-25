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

    private CollectionsModifiedListener collectionsModifiedListener;

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
            if (collectionsModifiedListener != null) {
                collectionsModifiedListener.onCollectionAdded(object, collectionList.size() - 1);
            }
        }

        @Override
        public void onUpdate(Collection object) {

        }

        @Override
        public void onDelete(Collection object) {
            int index = collectionList.indexOf(object);
            if (index != -1) {
                collectionList.remove(object);
                notifyDataSetChanged();
                if (collectionsModifiedListener != null) {
                    collectionsModifiedListener.onCollectionRemoved(object, index);
                }
            }
        }
    };

    public ImageStreamViewPagerAdapter(
            FragmentManager fm,
            CollectionRepository collectionRepository
    ) {
        super(fm);
        this.collectionRepository = collectionRepository;
        this.collectionList = collectionRepository.getAll();
        collectionRepository.addListener(repositoryCallback);
    }

    @Override
    public Fragment getItem(int position) {
        return ImageStreamFragment.newInstance(position);
    }
    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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

    public void setCollectionsModifiedListener(CollectionsModifiedListener collectionsModifiedListener) {
        this.collectionsModifiedListener = collectionsModifiedListener;
    }

    public interface CollectionsModifiedListener {

        void onCollectionAdded(Collection collection, int position);

        void onCollectionRemoved(Collection collection, int position);

    }
}
