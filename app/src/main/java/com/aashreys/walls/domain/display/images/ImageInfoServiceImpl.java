package com.aashreys.walls.domain.display.images;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.network.apis.FlickrApi;
import com.aashreys.walls.network.apis.UnsplashApi;
import com.aashreys.walls.network.parsers.FlickrExifParser;
import com.aashreys.walls.network.parsers.UnsplashPhotoInfoParser;
import com.aashreys.walls.network.parsers.Utils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aashreys on 05/03/17.
 */

public class ImageInfoServiceImpl implements ImageInfoService {

    private static final String TAG = ImageInfoServiceImpl.class.getSimpleName();

    private final FlickrApi flickrApi;

    private final UnsplashApi unsplashApi;

    private final FlickrExifParser flickrExifParser;

    private final UnsplashPhotoInfoParser unsplashPhotoInfoParser;

    public ImageInfoServiceImpl(
            UnsplashApi unsplashApi,
            FlickrApi flickrApi,
            UnsplashPhotoInfoParser unsplashPhotoInfoParser,
            FlickrExifParser flickrExifParser
    ) {
        this.flickrApi = flickrApi;
        this.unsplashApi = unsplashApi;
        this.flickrExifParser = flickrExifParser;
        this.unsplashPhotoInfoParser = unsplashPhotoInfoParser;
    }

    @Override
    public void addInfo(Image image, Listener listener) {


        Name serviceName = image.getInfo().serviceName;
        if (serviceName.equals(FlickrImage.SERVICE_NAME)) {
            addFlickrInfo(image, listener);
        } else if (serviceName.equals(UnsplashImage.SERVICE_NAME)) {
            addUnsplashInfo(image, listener);
        } else {
            listener.onComplete(image);
        }
    }

    private void addUnsplashInfo(final Image image, final Listener listener) {
        Call<ResponseBody> call = unsplashApi.getPhoto(image.getId().value());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        unsplashPhotoInfoParser.parse(
                                response.body().string(),
                                image
                        );

                    } catch (IOException e) {
                        LogWrapper.e(
                                TAG,
                                "Could not set Unsplash info, request failed with code " +
                                        response.code(), e
                        );
                    }
                } else {
                    LogWrapper.e(
                            TAG,
                            "Could not set Unsplash info, request failed with code " +
                                    response.code()
                    );
                }
                listener.onComplete(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onComplete(image);
            }
        });
    }

    private void addFlickrInfo(final Image image, final Listener listener) {
        Call<ResponseBody> call = flickrApi.getPhotoExif(image.getId().value());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        image.getInfo().exif =
                                flickrExifParser.parse(Utils.removeFlickrResponseBrackets(
                                        response.body().string()
                                ));
                    } catch (IOException e) {
                        LogWrapper.e(
                                TAG,
                                "Could not set Flickr info, request failed with code " +
                                        response.code(), e
                        );
                    }
                } else {
                    LogWrapper.e(
                            TAG,
                            "Could not set Flickr info, request failed with code " +
                                    response.code()
                    );
                }
                listener.onComplete(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onComplete(image);
            }
        });
    }
}
