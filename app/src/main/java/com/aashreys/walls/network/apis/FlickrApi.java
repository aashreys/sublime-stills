package com.aashreys.walls.network.apis;

import android.support.annotation.IntRange;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by aashreys on 01/03/17.
 */

public interface FlickrApi {

    String ENDPOINT = "https://api.flickr.com/services/rest/";

    String PHOTO_INFO_CACHE_CONTROL = "max-age=3600";

    String GENERAL_CACHE_CONTROL = "max-age=300";

    @Headers("Cache-Control: " + GENERAL_CACHE_CONTROL)
    @GET("?method=flickr.interestingness.getList")
    Call<ResponseBody> getInterestingPhotos(
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 500) @Query("per_page") int itemsPerPage
    );

    @Headers("Cache-Control: " + GENERAL_CACHE_CONTROL)
    @GET("?method=flickr.photos.search")
    Call<ResponseBody> searchPhotos(
            @Query("tags") String tag,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 500) @Query("per_page") int itemsPerPage
    );

    @Headers("Cache-Control: " + GENERAL_CACHE_CONTROL)
    @GET("?method=flickr.tags.getRelated")
    Call<ResponseBody> getRelatedTags(
            @Query("tag") String tag
    );

    @Headers("Cache-Control: " + PHOTO_INFO_CACHE_CONTROL)
    @GET("?method=flickr.photos.getInfo")
    Call<ResponseBody> getPhotoInfo(
            @Query("photo_id") String photoId
    );

}
