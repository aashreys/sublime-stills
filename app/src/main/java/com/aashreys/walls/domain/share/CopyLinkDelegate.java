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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;

/**
 * Created by aashreys on 20/03/17.
 */

public class CopyLinkDelegate implements ShareDelegate {

    @Override
    public void share(
            Context context, Image image, Listener listener
    ) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                context.getResources().getString(R.string.title_image_link_clipboard_label),
                image.getShareUrl().value()
        );
        clipboard.setPrimaryClip(clip);
        listener.onShareComplete();
    }

    @Override
    public void cancel() {

    }
}
