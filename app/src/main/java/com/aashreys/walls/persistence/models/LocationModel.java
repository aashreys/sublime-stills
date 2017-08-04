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

import com.aashreys.walls.DontObfuscate;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.values.Value;

/**
 * Created by aashreys on 23/04/17.
 */

@DontObfuscate
public class LocationModel {

    private final String name;

    private final CoordinatesModel coordinatesModel;

    public LocationModel(Location location) {
        this.name = Value.getValidValue(location.getName());
        if (location.getCoordinates() != null) {
            this.coordinatesModel = new CoordinatesModel(location.getCoordinates());
        } else {
            this.coordinatesModel = null;
        }
    }

    public String getName() {
        return name;
    }

    public CoordinatesModel getCoordinatesModel() {
        return coordinatesModel;
    }
}
