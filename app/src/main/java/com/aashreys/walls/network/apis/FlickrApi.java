package com.aashreys.walls.network.apis;

import android.support.annotation.IntRange;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aashreys on 01/03/17.
 */

public interface FlickrApi {

    String ENDPOINT = "https://api.flickr.com/services/rest/";

    String PHOTO_INFO_CACHE_CONTROL = "max-age=3600";

    String GENERAL_CACHE_CONTROL = "max-age=300";

    String PHOTO_EXTRAS = "date_taken, owner_name, geo";

    @GET("?method=flickr.interestingness.getList")
    Call<ResponseBody> getInterestingPhotos(
            @Query("extras") String extras,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 500) @Query("per_page") int itemsPerPage
    );

    @GET("?method=flickr.photos.search")
    Call<ResponseBody> searchPhotos(
            @Query("tags") String tag,
            @Query("extras") String extras,
            @Query("page") int pageNumber,
            @IntRange(from = 1, to = 500) @Query("per_page") int itemsPerPage
    );

    @GET("?method=flickr.tags.getRelated")
    Call<ResponseBody> getRelatedTags(
            @Query("tag") String tag
    );

    @GET("?method=flickr.photos.getExif")
    Call<ResponseBody> getPhotoExif(
            @Query("photo_id") String photoId
    );

}
