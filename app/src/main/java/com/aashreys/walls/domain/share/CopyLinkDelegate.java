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
import com.aashreys.walls.domain.share.actions.CopyLinkAction;

import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * Created by aashreys on 20/03/17.
 */

public class CopyLinkDelegate implements ShareDelegate {

    private final CopyLinkAction copyLinkAction;

    public CopyLinkDelegate(CopyLinkAction copyLinkAction) {
        this.copyLinkAction = copyLinkAction;
    }

    @Override
    public Completable share(final Context context, final Image image) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                copyLinkAction.copy(context, image.getShareUrl());
            }
        });
    }
}
