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

/**
 * Created by aashreys on 21/03/17.
 */

@AutoFactory
public class FeaturedCollectionsTask extends AsyncTask<Void, Void, List<Collection>> {

    private CollectionSearchService collectionSearchService;

    private CollectionSearchTask.CollectionSearchListener listener;

    public FeaturedCollectionsTask(@Provided CollectionSearchService collectionSearchService
    ) {
        this.collectionSearchService = collectionSearchService;
    }

    public void setListener(CollectionSearchTask.CollectionSearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Collection> doInBackground(Void... params) {
        return collectionSearchService.getFeatured();
    }

    @Override
    protected void onPostExecute(List<Collection> collections) {
        if (listener != null) {
            listener.onSearchComplete(collections);
        }
    }
}
