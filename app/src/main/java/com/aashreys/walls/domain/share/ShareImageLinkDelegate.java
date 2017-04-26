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

import android.content.Context;

import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.actions.ShareImageLinkAction;
import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 08/02/17.
 */

public class ShareImageLinkDelegate implements ShareDelegate {

    private static final String TAG = ShareImageLinkDelegate.class.getSimpleName();

    private final ShareImageLinkAction shareImageLinkAction;

    public ShareImageLinkDelegate(ShareImageLinkAction shareImageLinkAction) {
        this.shareImageLinkAction = shareImageLinkAction;
    }

    @Override
    public void share(final Context context, final Image image, final Listener listener) {
        Url imageUrl = image.getShareUrl();
        shareImageLinkAction.shareImageLink(context, image.getTitle(), imageUrl);
        listener.onShareComplete();
    }

    @Override
    public void cancel() {

    }
}
