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
import android.graphics.drawable.Drawable;

import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.utils.SchedulerProvider;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.ShareDelegate.Mode;
import com.aashreys.walls.domain.share.actions.CopyLinkAction;
import com.aashreys.walls.domain.share.actions.SetAsAction;
import com.aashreys.walls.domain.share.actions.ShareImageLinkAction;
import com.aashreys.walls.domain.share.mocks.MockImageDownloader;
import com.aashreys.walls.domain.share.mocks.MockShareActionFactory;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aashreys on 05/04/17.
 */

public class ShareDelegateTests extends MockitoTestCase {

    private static final int DEVICE_WIDTH = 100;

    private static final Name IMAGE_TITLE = new Name("Mister Howes");

    private static final Url IMAGE_SHARE_URL = new Url("https://www.someurl.com");

    @Mock DeviceResolution deviceResolution;

    @Mock CopyLinkAction copyLinkAction;

    @Mock SetAsAction setAsAction;

    @Mock ShareImageLinkAction shareImageLinkAction;

    @Mock Context context;

    @Mock Image image;

    @Mock Drawable mockDrawable;

    @Mock File mockFile;

    private MockImageDownloader mockImageDownloader;

    private ShareDelegateFactory shareDelegateFactory;

    private SchedulerProvider mockSchedulerProvider = new SchedulerProvider() {

        @Override
        public Scheduler mainThread() {
            return Schedulers.newThread();
        }

    };

    @Override
    public void init() {
        super.init();
        Mockito.when(image.getTitle()).thenReturn(IMAGE_TITLE);
        Mockito.when(image.getShareUrl()).thenReturn(IMAGE_SHARE_URL);
        Mockito.when(deviceResolution.getWidth()).thenReturn(DEVICE_WIDTH);

        MockShareActionFactory mockShareActionFactory = new MockShareActionFactory();
        mockShareActionFactory.setMockActions(
                copyLinkAction,
                setAsAction,
                shareImageLinkAction
        );

        mockImageDownloader = new MockImageDownloader();
        mockImageDownloader.setMockDrawable(mockDrawable);
        mockImageDownloader.setMockFile(mockFile);

        shareDelegateFactory = new ShareDelegateFactory(
                deviceResolution,
                mockShareActionFactory,
                mockImageDownloader,
                mockSchedulerProvider
        );
    }

    @Test
    public void test_copy_link_delegate() {
        CopyLinkDelegate copyLinkShareDelegate =
                (CopyLinkDelegate) shareDelegateFactory.create(Mode.COPY_LINK);
        copyLinkShareDelegate.share(context, image).blockingAwait();
        Mockito.verify(copyLinkAction).copy(context, image.getShareUrl());
    }

    @Test
    public void test_set_as_delegate() {
        SetAsDelegate setAsDelegate = (SetAsDelegate) shareDelegateFactory.create(Mode.SET_AS);
        setAsDelegate.share(context, image).blockingAwait();
        Mockito.verify(image).getUrl(DEVICE_WIDTH * 2);
        Mockito.verify(setAsAction).setAs(context, mockFile);
    }

    @Test
    public void test_share_image_link_delegate() {
        ShareImageLinkDelegate shareImageLinkDelegate =
                (ShareImageLinkDelegate) shareDelegateFactory.create(Mode.LINK);
        shareImageLinkDelegate.share(context, image).blockingAwait();
        Mockito.verify(shareImageLinkAction).shareImageLink(context, IMAGE_TITLE, IMAGE_SHARE_URL);
    }
}
