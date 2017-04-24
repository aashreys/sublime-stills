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

package com.aashreys.walls.ui.helpers;

import android.support.annotation.DrawableRes;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.ResourceProvider;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.metadata.Coordinates;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.ui.views.InfoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 15/04/17.
 */
public class ImageInfoBuilder {

    private final List<InfoView.Info> infoList;

    private Image image;

    @Inject ResourceProvider resourceProvider;

    @Inject
    public ImageInfoBuilder() {
        this.infoList = new ArrayList<>();
    }

    private void initialize(Image image) {
        this.image = image;
        this.infoList.clear();
    }

    public List<InfoView.Info> buildInfoList(Image image) {
        initialize(image);
        addImageProperties();
        return infoList;
    }

    private void addImageProperties() {
        addDate();
        addLocation();
        addResolution();
        addExif();
    }

    private void addExif() {
        Exif exif = image.getExif();
        if (exif != null) {
            addCamera(exif);
            addAperture(exif);
            addExposureTime(exif);
            addFocalLength(exif);
            addIso(exif);
        }
    }

    private void addIso(Exif exif) {
        addInfo(R.drawable.ic_iso_black_24dp, Value.getValidValue(exif.iso));
    }

    private void addFocalLength(Exif exif) {
        addInfo(R.drawable.ic_focus_black_24dp, Value.getValidValue(exif.focalLength));
    }

    private void addExposureTime(Exif exif) {
        addInfo(R.drawable.ic_exposure_time_black_24dp, Value.getValidValue(exif.exposureTime));
    }

    private void addAperture(Exif exif) {
        addInfo(R.drawable.ic_aperture_black_24dp, Value.getValidValue(exif.aperture));
    }

    private void addCamera(Exif exif) {
        addInfo(R.drawable.ic_camera_black_24dp, Value.getValidValue(exif.camera));
    }


    private void addResolution() {
        addInfo(
                R.drawable.ic_dimensions_black_24dp,
                getFormattedResolutionString(image.getResolution())
        );
    }

    private String getFormattedResolutionString(Resolution resolution) {
        String resolutionString = null;
        if (resolution != null) {
            resolutionString = resourceProvider.getString(
                    R.string.resolution_formatted_string,
                    resolution.getWidth().value(),
                    resolution.getHeight().value()
            );
        }
        return resolutionString;
    }

    private void addLocation() {
        addInfo(R.drawable.ic_location_pin_black_24dp, getLocationName(image.getLocation()));
    }

    private void addDate() {
        addInfo(R.drawable.ic_date_black_24dp, getFormattedDateString(image.getUploadDate()));
    }

    private String getLocationName(Location location) {
        String locationName = null;
        if (location != null) {
            if (location.getName() != null) {
                locationName = location.getName().value();
            } else if (location.getCoordinates() != null) {
                locationName = formatLocationCoordinates(location.getCoordinates());
            }
        }
        return locationName;
    }

    private String formatLocationCoordinates(Coordinates coordinates) {
        if (coordinates != null) {
            return coordinates.getLatitude() + " " + coordinates.getLongitude();
        } else {
            return null;
        }
    }

    private String getFormattedDateString(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }

    private void addInfo(@DrawableRes int iconRes, String info) {
        if (iconRes != 0 && info != null) {
            infoList.add(new InfoView.Info(iconRes, info));
        }
    }

}
