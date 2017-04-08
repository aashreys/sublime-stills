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

package com.aashreys.walls.ui;

import com.aashreys.walls.domain.display.images.Image;

/**
 * Created by aashreys on 08/04/17.
 */

interface StreamActivityModelEventListener {

    void onSettingsNavigationItemSelected();

    void onCollectionsNavigationItemSelected();

    void onNewTabSelected(int tabPosition);

    void onImageFavoriteButtonClicked(Image image, boolean isFavorite);

    void onImageClicked(Image image);

    void onMenuButtonClicked();

    void onAddCollectionsButtonClicked();

    void onCollectionsModified();

}
