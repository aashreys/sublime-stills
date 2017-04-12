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

import com.aashreys.walls.ui.StreamFragment;

/**
 * Created by aashreys on 05/02/17.
 */
public class StreamViewPagerAdapter extends FragmentStatePagerAdapter {

    private StreamAdapter.CollectionProvider collectionProvider;

    public StreamViewPagerAdapter(
            FragmentManager fm,
            StreamAdapter.CollectionProvider collectionProvider
    ) {
        super(fm);
        this.collectionProvider = collectionProvider;
    }

    @Override
    public Fragment getItem(int position) {
        return StreamFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return collectionProvider.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return collectionProvider.getCollection(position).getName().value();
    }
}
