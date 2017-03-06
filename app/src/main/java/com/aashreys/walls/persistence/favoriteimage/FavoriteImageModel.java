package com.aashreys.walls.persistence.favoriteimage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.persistence.WallsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by aashreys on 06/12/16.
 */

@Table(database = WallsDatabase.class)
public class FavoriteImageModel extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long _id;

    /**
     * Image Server ID
     */
    @NonNull
    @Column
    String id;

    @Nullable
    @Column
    String title;

    @Nullable
    @Column
    Integer resX;

    @Nullable
    @Column
    Integer resY;

    @Nullable
    @Column
    Date createdAt;

    @Nullable
    @Column
    String userRealName;

    @Nullable
    @Column
    String userProfileUrl;

    @Nullable
    @Column
    String userPortfolioUrl;

    @NonNull
    @Column
    String imageStreamUrl;

    @NonNull
    @Column
    String imageDetailUrl;

    @NonNull
    @Column
    String imageSetAsUrl;

    @NonNull
    @Column
    String imageShareUrl;

    @NonNull
    @Column
    String serviceName;

    @Nullable
    @Column
    String serviceUrl;

    public FavoriteImageModel() {}

    FavoriteImageModel(Image image) {
        this.id = image.getId().value();
        this.imageStreamUrl = image.getUrl(Image.UrlType.IMAGE_STREAM).value();
        this.imageDetailUrl = image.getUrl(Image.UrlType.IMAGE_DETAIL).value();
        this.imageSetAsUrl = image.getUrl(Image.UrlType.SET_AS).value();
        this.imageShareUrl = image.getUrl(Image.UrlType.SHARE).value();
        Image.Properties properties = image.getProperties();
        this.resX = Value.getNullableValue(properties.resX);
        this.resY = Value.getNullableValue(properties.resY);
        this.createdAt = properties.createdAt;
        this.userRealName = Value.getNullableValue(properties.userRealName);
        this.userProfileUrl = Value.getNullableValue(properties.userProfileUrl);
        this.userPortfolioUrl = Value.getNullableValue(properties.userPortfolioUrl);
        this.serviceName = properties.serviceName.value();
        this.serviceUrl = Value.getNullableValue(properties.serviceUrl);
    }

}
