package com.aashreys.walls.domain.display.collections.search;

import android.support.annotation.NonNull;

import com.aashreys.walls.domain.display.collections.Collection;

import java.util.List;

/**
 * Created by aashreys on 05/03/17.
 */

public interface CollectionSearchService {

    @NonNull
    List<Collection> search(String collection);

}
