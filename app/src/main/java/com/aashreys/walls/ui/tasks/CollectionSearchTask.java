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

package com.aashreys.walls.ui.tasks;

import android.os.AsyncTask;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.search.CollectionSearchService;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/02/17.
 */

@AutoFactory
public class CollectionSearchTask extends AsyncTask<String, Void, List<Collection>> {

    private CollectionSearchService collectionSearchService;

    private CollectionSearchListener listener;

    @Inject
    public CollectionSearchTask(@Provided CollectionSearchService unsplashCollectionSearcher) {
        this.collectionSearchService = unsplashCollectionSearcher;
    }

    public void setListener(CollectionSearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Collection> doInBackground(String... searchStrings) {
        return collectionSearchService.search(searchStrings[0]);
    }

    @Override
    protected void onPostExecute(List<Collection> collections) {
        if (listener != null) {
            listener.onSearchComplete(collections);
        }
    }

    public interface CollectionSearchListener {

        void onSearchComplete(List<Collection> collections);

    }
}
