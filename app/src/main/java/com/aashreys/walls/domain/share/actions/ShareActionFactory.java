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

package com.aashreys.walls.domain.share.actions;

import javax.inject.Inject;

/**
 * Created by aashreys on 05/04/17.
 */

public class ShareActionFactory {

    @Inject
    public ShareActionFactory() {}

    public CopyLinkAction createCopyLinkAction() {
        return new CopyLinkAction();
    }

    public SetAsAction createSetAsAction() {
        return new SetAsAction();
    }

    public ShareImageLinkAction createShareImageLinkAction() {
        return new ShareImageLinkAction();
    }

    public SetWallpaperAction createSetWallpaperAction() {
        return new SetWallpaperAction();
    }

}
