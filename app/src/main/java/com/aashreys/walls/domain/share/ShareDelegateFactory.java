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

package com.aashreys.walls.domain.share;

import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.share.actions.ShareActionFactory;
import com.aashreys.walls.network.UrlShortener;

import javax.inject.Inject;

/**
 * Created by aashreys on 09/02/17.
 */

public class ShareDelegateFactory {

    private final DeviceResolution deviceResolution;

    private final UrlShortener urlShortener;

    private final ShareActionFactory shareActionFactory;

    @Inject
    public ShareDelegateFactory(
            UrlShortener urlShortener,
            DeviceResolution deviceResolution,
            ShareActionFactory shareActionFactory
    ) {
        this.urlShortener = urlShortener;
        this.deviceResolution = deviceResolution;
        this.shareActionFactory = shareActionFactory;
    }

    public ShareDelegate create(ShareDelegate.Mode mode) {
        switch (mode) {
            case LINK:
                return new ShareImageLinkDelegate(
                        urlShortener,
                        shareActionFactory.createShareImageLinkAction()
                );
            case COPY_LINK:
                return new CopyLinkDelegate(shareActionFactory.createCopyLinkAction());
            case PHOTO:
                return new ShareImageDelegate(
                        deviceResolution,
                        shareActionFactory.createShareImageAction()
                );
            case SET_AS:
                return new SetAsDelegate(deviceResolution, shareActionFactory.createSetAsAction());
        }
        throw new IllegalArgumentException("Unexpected share mode");
    }
}
