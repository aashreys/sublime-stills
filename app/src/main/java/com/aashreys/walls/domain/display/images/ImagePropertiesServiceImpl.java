package com.aashreys.walls.domain.display.images;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.apis.FlickrApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aashreys on 05/03/17.
 */

public class ImagePropertiesServiceImpl implements ImagePropertiesService {

    private static final String TAG = ImagePropertiesServiceImpl.class.getSimpleName();

    // Formatting argument: user_id
    private static final String
            TEMPLATE_USER_PROFILE = "https://www.flickr.com/people/%s/",
            TEMPLATE_USER_PORTFOLIO = "https://www.flickr.com/photos/%s/";

    private final FlickrApi flickrApi;

    public ImagePropertiesServiceImpl(FlickrApi flickrApi) {
        this.flickrApi = flickrApi;
    }

    @Override
    public void addProperties(Image image, Listener listener) {
        if (image.getProperties().serviceName.equals(FlickrImage.SERVICE_NAME)) {
            addFlickrProperties(image, listener);
        } else {
            listener.onComplete(image);
        }
    }

    private void addFlickrProperties(final Image image, final Listener listener) {
        Call<ResponseBody> call = flickrApi.getPhotoInfo(image.getId().value());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        setFlickrProperties(response.body().string(), image.getProperties());
                    } catch (IOException e) {
                        LogWrapper.e(
                                TAG,
                                "Could not fetch image properties. Request failed with unexpected " +
                                        "error code " +
                                        response.code(),
                                e
                        );
                    }
                } else {
                    LogWrapper.e(
                            TAG,
                            "Could not fetch image properties. Request failed with unexpected " +
                                    "error code " +
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

    private void setFlickrProperties(String response, Image.Properties properties) {
        try {
            response = response.substring(response.indexOf('{'), response.lastIndexOf('}') + 1);
            JSONObject photoJson = new JSONObject(response).getJSONObject("photo");
            properties.title = new Name(photoJson.getJSONObject("title").getString("_content"));
            properties.createdAt = new Date(Long.valueOf(photoJson.getString("dateuploaded") + "000"));

            JSONObject userJson = photoJson.getJSONObject("owner");
            properties.userId = new Id(userJson.getString("nsid"));
            properties.userRealName = new Name(userJson.getString("realname"));
            properties.userProfileUrl = new Url(String.format(
                    TEMPLATE_USER_PROFILE,
                    properties.userId.value()
            ));
            properties.userPortfolioUrl = new Url(String.format(
                    TEMPLATE_USER_PORTFOLIO,
                    properties.userId.value()
            ));
        } catch (JSONException e) {
            LogWrapper.e(TAG, "Could not set flickr properties", e);
        }
    }
}
