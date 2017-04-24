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

package com.aashreys.walls.persistence.models;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.InstantiationException;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.UnsplashImage;
import com.aashreys.walls.domain.display.images.metadata.Coordinates;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Color;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.utils.LogWrapper;

import java.util.Date;

/**
 * Created by aashreys on 23/04/17.
 */

public class UnsplashImageModel implements ImageModel {

    private static final String TAG = UnsplashImageModel.class.getSimpleName();

    @NonNull
    private final String id;

    @NonNull
    private final String rawImageUrl;

    @NonNull
    private final String imageShareUrl;

    @Nullable
    private final Date createdAt;

    @Nullable
    private final UserModel userModel;

    @Nullable
    private final ResolutionModel resolutionModel;

    @Nullable
    private final LocationModel locationModel;

    @Nullable
    private final ExifModel exifModel;

    @Nullable
    private final String title;

    @ColorInt
    private final int backgroundColor;

    public UnsplashImageModel(UnsplashImage image) {
        this.id = image.getId().value();
        this.rawImageUrl = image.rawImageUrl.value();
        this.imageShareUrl = image.getShareUrl().value();
        this.createdAt = image.getUploadDate();
        this.userModel = image.getUser() != null ? new UserModel(image.getUser()) : null;
        this.resolutionModel =
                image.getResolution() != null ? new ResolutionModel(image.getResolution()) : null;
        this.locationModel =
                image.getLocation() != null ? new LocationModel(image.getLocation()) : null;
        this.exifModel = image.getExif() != null ? new ExifModel(image.getExif()) : null;
        this.title = Value.getValidValue(image.getTitle());
        this.backgroundColor = Value.getValidValue(image.getBackgroundColor(), 0);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getRawImageUrl() {
        return rawImageUrl;
    }

    @NonNull
    public String getImageShareUrl() {
        return imageShareUrl;
    }

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public UserModel getUserModel() {
        return userModel;
    }

    @Nullable
    public ResolutionModel getResolutionModel() {
        return resolutionModel;
    }

    @Nullable
    public LocationModel getLocationModel() {
        return locationModel;
    }

    @Nullable
    public ExifModel getExifModel() {
        return exifModel;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public Image createImage() {
        User user = createUser();
        Location location = createLocation();
        Exif exif = createExif();
        Resolution resolution = createResolution();
        try {
            return new UnsplashImage(
                    new Id(id),
                    new Url(rawImageUrl),
                    new Url(imageShareUrl),
                    new Name(title),
                    createdAt,
                    user,
                    location,
                    exif,
                    resolution,
                    new Color(backgroundColor)
            );
        } catch (InstantiationException e) {
            logCreationError(e);
            return null;
        }

    }

    private Resolution createResolution() {
        Resolution resolution = null;
        if (resolutionModel != null) {
            try {
                resolution = new Resolution(
                        new Pixel(resolutionModel.getWidth()),
                        new Pixel(resolutionModel.getHeight())
                );
            } catch (InstantiationException e) {
                logCreationError(e);
            }
        }
        return resolution;
    }

    private Exif createExif() {
        Exif exif = null;
        if (exifModel != null) {
            try {
                exif = new Exif(
                        new Name(exifModel.getCamera()),
                        new Name(exifModel.getExposureTime()),
                        new Name(exifModel.getAperture()),
                        new Name(exifModel.getFocalLength()),
                        new Name(exifModel.getIso())
                );
            } catch (InstantiationException e) {
                logCreationError(e);
            }
        }
        return exif;
    }

    private Location createLocation() {
        Location location = null;
        if (locationModel != null) {
            try {
                location = new Location(
                        new Name(locationModel.getName()),
                        createCoordinates()
                );
            } catch (InstantiationException e) {
                logCreationError(e);
            }
        }
        return location;
    }

    private Coordinates createCoordinates() {
        Coordinates coordinates = null;
        if (locationModel != null && locationModel.getCoordinatesModel() != null) {
            try {
                coordinates = new Coordinates(
                        locationModel.getCoordinatesModel().getLatitude(),
                        locationModel.getCoordinatesModel().getLongitude()
                );
            } catch (InstantiationException e) {
                logCreationError(e);
            }
        }
        return coordinates;
    }

    private User createUser() {
        User user = null;
        if (userModel != null) {
            try {
                user = new User(
                        new Id(userModel.getId()),
                        new Name(userModel.getName()),
                        new Url(userModel.getProfileUrl()),
                        new Url(userModel.getPortfolioUrl())
                );
            } catch (InstantiationException e) {
                logCreationError(e);
            }
        }
        return user;
    }

    private void logCreationError(InstantiationException e) {
        LogWrapper.wtf(TAG, e);
    }
}
