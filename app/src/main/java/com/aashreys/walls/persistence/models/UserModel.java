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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.values.Value;

/**
 * Created by aashreys on 23/04/17.
 */

public class UserModel {

    @NonNull
    private final String id;

    @Nullable
    private final String name;

    @Nullable
    private final String profileUrl;

    @Nullable
    private final String portfolioUrl;

    public UserModel(User user) {
        this.id = user.getId().value();
        this.name = Value.getValidValue(user.getName());
        this.profileUrl = Value.getValidValue(user.getProfileUrl());
        this.portfolioUrl = Value.getValidValue(user.getPortfolioUrl());
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getProfileUrl() {
        return profileUrl;
    }

    @Nullable
    public String getPortfolioUrl() {
        return portfolioUrl;
    }
}
