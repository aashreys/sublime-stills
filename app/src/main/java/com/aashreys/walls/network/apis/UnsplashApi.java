package com.aashreys.walls.network.apis;

import android.support.annotation.IntRange;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aashreys on 01/03/17.
 */

public interface UnsplashApi {

    String ENDPOINT = "https://api.unsplash.com/";

    String PHOTO_INFO_CACHE_DURATION = "max-age=3600";

    String GENERAL_CACHE_DURATION = "max-age=300";

    @GET("photos")
    Call<ResponseBody> getRecentPhotos(
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    ) throws IOException;

    @GET("collections/{id}/photos")
    Call<ResponseBody> getCollectionPhotos(
            @Path("id") String collectionId,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    ) throws IOException;

    @GET("search/collections")
    Call<ResponseBody> searchCollections(
            @Query("query") String searchString,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 30) @Query("per_page") int imagesPerPage
    ) throws IOException;

    @GET("photos/{id}")
    Call<ResponseBody> getPhoto(
            @Path("id") String photoId
    );

}
