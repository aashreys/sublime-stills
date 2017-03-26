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

    int ITEMS_PER_PAGE = 250;

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
