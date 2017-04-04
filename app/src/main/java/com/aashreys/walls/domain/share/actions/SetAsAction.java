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

import java.io.File;

/**
 * Created by aashreys on 05/04/17.
 */

public class SetAsAction {

    private static final String MIME_TYPE = "image/*";

    public void setAs(Context context, File fileToSet) {
        Uri imageUri = Uri.fromFile(fileToSet);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(imageUri, MIME_TYPE);
        intent.putExtra("mimeType", MIME_TYPE);
        context.startActivity(Intent.createChooser(
                intent,
                context.getString(R.string.share_set_as_title)
        ));
    }

}
