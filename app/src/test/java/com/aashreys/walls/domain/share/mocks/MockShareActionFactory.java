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

package com.aashreys.walls.domain.share.mocks;

import com.aashreys.walls.domain.share.actions.CopyLinkAction;
import com.aashreys.walls.domain.share.actions.SetAsAction;
import com.aashreys.walls.domain.share.actions.ShareActionFactory;
import com.aashreys.walls.domain.share.actions.ShareImageAction;
import com.aashreys.walls.domain.share.actions.ShareImageLinkAction;

/**
 * Created by aashreys on 05/04/17.
 */

public class MockShareActionFactory extends ShareActionFactory {

    private CopyLinkAction copyLinkAction;

    private SetAsAction setAsAction;

    private ShareImageAction shareImageAction;

    private ShareImageLinkAction shareImageLinkAction;

    public void setMockActions(
            CopyLinkAction copyLinkAction,
            SetAsAction setAsAction,
            ShareImageAction shareImageAction,
            ShareImageLinkAction shareImageLinkAction
    ) {
        this.copyLinkAction = copyLinkAction;
        this.setAsAction = setAsAction;
        this.shareImageAction = shareImageAction;
        this.shareImageLinkAction = shareImageLinkAction;
    }

    @Override
    public CopyLinkAction createCopyLinkAction() {
        return copyLinkAction;
    }

    @Override
    public SetAsAction createSetAsAction() {
        return setAsAction;
    }

    @Override
    public ShareImageAction createShareImageAction() {
        return shareImageAction;
    }

    @Override
    public ShareImageLinkAction createShareImageLinkAction() {
        return shareImageLinkAction;
    }

}
