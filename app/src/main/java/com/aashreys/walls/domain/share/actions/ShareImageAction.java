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
import android.net.Uri;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.metadata.User;

import java.io.File;

/**
 * Created by aashreys on 05/04/17.
 */

public class ShareImageAction {

    private static final String MIME_IMAGE = "image/*";

    public void shareImage(Context context, Image image, File file) {
        String shareText = buildShareText(context, image);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        if (shareText != null) {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        intent.setType(MIME_IMAGE);
        context.startActivity(Intent.createChooser(
                intent,
                context.getResources().getText(R.string.title_share_photo)
        ));
    }

    private String buildShareText(Context context, Image image) {
        User user = image.getUser();
        String shareText = null;
        if (user != null && user.getName() != null) {
            String imageUrl = image.getShareUrl().value();
            String userName = user.getName().value();
            String userUrl = null;
            if (user.getProfileUrl() != null) {
                userUrl = user.getProfileUrl().value();
            }
            if (userUrl == null && user.getPortfolioUrl() != null) {
                userUrl = user.getPortfolioUrl().value();
            }

            if (userUrl != null) {
                shareText = context.getString(
                        R.string.share_file_template,
                        imageUrl,
                        userName,
                        userUrl
                );
            } else {
                shareText = context.getString(
                        R.string.share_file_template_nouserurl,
                        imageUrl,
                        userName
                );
            }
        }
        return shareText;
    }

}
