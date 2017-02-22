package com.aashreys.walls.persistence.favoriteimage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.Image;
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
    long localId;

    @NonNull
    @Column
    String serverId;

    @Nullable
    @Column
    Integer resX;

    @Nullable
    @Column
    Integer resY;

    @NonNull
    @Column
    Date createdAt;

    @Nullable
    @Column
    String photographerName;

    @Nullable
    @Column
    String userProfileUrl;

    @Nullable
    @Column
    String userPortfolioUrl;

    @NonNull
    @Column
    String smallImageUrl;

    @NonNull
    @Column
    String regularImageUrl;

    @NonNull
    @Column
    String fullImageUrl;

    @NonNull
    @Column
    String serviceName;

    @Nullable
    @Column
    String serviceUrl;

    public long getLocalId() {
        return localId;
    }

    @NonNull
    String getServerId() {
        return serverId;
    }

    @Nullable
    Integer getResX() {
        return resX;
    }

    @Nullable
    Integer getResY() {
        return resY;
    }

    @NonNull
    Date getCreatedAt() {
        return createdAt;
    }

    @Nullable
    String getPhotographerName() {
        return photographerName;
    }

    @Nullable
    String getUserProfileUrl() {
        return userProfileUrl;
    }

    @Nullable
    String getUserPortfolioUrl() {
        return userPortfolioUrl;
    }

    @NonNull
    String getSmallImageUrl() {
        return smallImageUrl;
    }

    @NonNull
    String getRegularImageUrl() {
        return regularImageUrl;
    }

    @NonNull
    String getFullImageUrl() {
        return fullImageUrl;
    }

    @NonNull
    String getServiceName() {
        return serviceName;
    }

    @Nullable
    String getServiceUrl() {
        return serviceUrl;
    }

    FavoriteImageModel() {}

    FavoriteImageModel(Image image) {
        this.serverId = image.serverId().value();
        this.resX = image.resX() != null ? image.resX().value() : null;
        this.resY = image.resY() != null ? image.resY().value() : null;
        this.createdAt = image.createdAt();
        this.photographerName = image.userRealName() != null ? image.userRealName()
                                                                    .value() : null;
        this.userProfileUrl = image.userProfileUrl() != null
                ? image.userProfileUrl().value()
                : null;
        this.userPortfolioUrl = image.userPortfolioUrl() != null ? image.userPortfolioUrl()
                .value() : null;
        this.smallImageUrl = image.smallImageUrl().value();
        this.regularImageUrl = image.regularImageUrl().value();
        this.fullImageUrl = image.fullImageUrl().value();
        this.serviceName = image.serviceName().value();
        this.serviceUrl = image.serviceUrl() != null ? image.serviceUrl().value() : null;
    }

}
