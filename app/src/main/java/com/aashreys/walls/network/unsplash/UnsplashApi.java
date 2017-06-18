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

package com.aashreys.walls.network.unsplash;

import android.support.annotation.IntRange;

import com.aashreys.walls.domain.display.images.Image;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aashreys on 01/03/17.
 */

public interface UnsplashApi {

    String ENDPOINT = "https://api.unsplash.com/";

    String PHOTO_INFO_CACHE_DURATION = "max-age=3600"; // 1 hour

    String GENERAL_CACHE_DURATION = "max-age=900"; // 15 minutes

    String FEATURED_COLLECTION_CACHE_DURATION = "max-age=7200"; // 2 hours

    int ITEMS_PER_PAGE = 30;

    String API_VERSION = "v1";

    @GET("photos")
    Single<List<Image>> getRecentPhotos(
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    );

    @GET("collections/{id}/photos")
    Single<List<Image>> getCollectionPhotos(
            @Path("id") String collectionId,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    );

    @GET("search/collections")
    Single<UnsplashCollectionSearchResponse> searchCollections(
            @Query("query") String searchString,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    );

    @GET("photos/{id}")
    Single<Image> getPhoto(
            @Path("id") String photoId
    );

    @GET("/collections/featured")
    Single<List<UnsplashCollectionResponse>> getFeaturedCollections(
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    );

}
