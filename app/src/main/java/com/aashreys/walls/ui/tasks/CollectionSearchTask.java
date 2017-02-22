package com.aashreys.walls.ui.tasks;

import android.os.AsyncTask;

import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.UnsplashCollectionSearcher;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 17/02/17.
 */

@AutoFactory
public class CollectionSearchTask extends AsyncTask<String, Void, List<Collection>> {

    private UnsplashCollectionSearcher collectionSearcher;

    private CollectionSearchListener listener;

    @Inject
    public CollectionSearchTask(@Provided UnsplashCollectionSearcher unsplashCollectionSearcher) {
        this.collectionSearcher = unsplashCollectionSearcher;
    }

    public void setListener(CollectionSearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Collection> doInBackground(String... searchStrings) {
        return collectionSearcher.search(searchStrings[0]);
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
