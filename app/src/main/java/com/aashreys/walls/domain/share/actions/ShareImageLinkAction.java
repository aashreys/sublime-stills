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

import android.content.Context;
import android.content.Intent;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 05/04/17.
 */

public class ShareImageLinkAction {

    private static final String MIME_TYPE = "text/plain";

    public void shareImageLink(Context context, Name imageTitle, Url imageUrl) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String contentString;
        if (imageTitle != null && imageTitle.isValid()) {
            contentString = context.getResources().getString(
                    R.string.share_text_template,
                    imageTitle.value(),
                    imageUrl.value()
            );
        } else {
            contentString = context.getResources().getString(
                    R.string.share_text_template_notitle,
                    imageUrl.value()
            );
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, contentString);
        shareIntent.setType(MIME_TYPE);
        context.startActivity(Intent.createChooser(
                shareIntent,
                context.getResources().getText(R.string.title_share_link)
        ));
    }
}
