package com.aashreys.walls.domain.display.sources;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.aashreys.walls.Config;
import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.UnsplashPhotoResponseParser;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by aashreys on 31/01/17.
 */

@AutoFactory
public class UnsplashCollectionSource implements Source {

    private static final String TAG = UnsplashCollectionSource.class.getSimpleName();

    private final UnsplashApi unsplashApi;

    private final UnsplashPhotoResponseParser responseParser;

    private final Id collectionId;

    public UnsplashCollectionSource(
            @Provided UnsplashApi unsplashApi,
            @Provided UnsplashPhotoResponseParser responseParser,
            Id collectionId
    ) {
        this.unsplashApi = unsplashApi;
        this.responseParser = responseParser;
        this.collectionId = collectionId;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public List<Image> getImages(int fromIndex) {
        try {
            Call<ResponseBody> call = unsplashApi.getCollectionPhotos(
                    collectionId.value(),
                    UiHelper.getPageNumber(fromIndex, Config.Unsplash.ITEMS_PER_PAGE),
                    Config.Unsplash.ITEMS_PER_PAGE
            );
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                return responseParser.parse(response.body().string());
            } else {
                throw new IOException("Unexpected response code " + response.code());
            }
        } catch (IOException e) {
            LogWrapper.e(TAG, "Unable to get images", e);
            return new ArrayList<>();
        }
    }
}
