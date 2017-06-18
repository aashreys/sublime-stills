package com.aashreys.walls.network.unsplash;

import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aashreys on 17/06/17.
 */
public class UnsplashCollectionResponse {

    private long id;

    @SerializedName("title")
    private String name;

    @SerializedName("total_photos")
    private int totalPhotos;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public UnsplashCollection toCollection() {
        return new UnsplashCollection(
                new Id(String.valueOf(id)),
                new Name(name)
        );
    }

}
