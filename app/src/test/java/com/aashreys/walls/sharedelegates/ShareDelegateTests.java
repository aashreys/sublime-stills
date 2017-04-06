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

package com.aashreys.walls.sharedelegates;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.aashreys.walls.MockitoTestCase;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.CopyLinkDelegate;
import com.aashreys.walls.domain.share.SetAsDelegate;
import com.aashreys.walls.domain.share.ShareDelegate;
import com.aashreys.walls.domain.share.ShareDelegate.Mode;
import com.aashreys.walls.domain.share.ShareDelegateFactory;
import com.aashreys.walls.domain.share.ShareImageDelegate;
import com.aashreys.walls.domain.share.ShareImageLinkDelegate;
import com.aashreys.walls.domain.share.actions.CopyLinkAction;
import com.aashreys.walls.domain.share.actions.SetAsAction;
import com.aashreys.walls.domain.share.actions.ShareImageAction;
import com.aashreys.walls.domain.share.actions.ShareImageLinkAction;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.sharedelegates.mocks.MockImageDownloader;
import com.aashreys.walls.sharedelegates.mocks.MockShareActionFactory;
import com.aashreys.walls.sharedelegates.mocks.MockUiHandlerFactory;
import com.aashreys.walls.sharedelegates.mocks.MockUrlShortener;
import com.aashreys.walls.ui.utils.UiHandler;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;

/**
 * Created by aashreys on 05/04/17.
 */

public class ShareDelegateTests extends MockitoTestCase {

    private static final int DEVICE_WIDTH = 100;

    private static final Name IMAGE_TITLE = new Name("Mister Howes");

    private static final Url IMAGE_SHARE_URL = new Url("https://www.someurl.com");

    private MockUrlShortener mockUrlShortener;

    @Mock DeviceResolution deviceResolution;

    @Mock CopyLinkAction copyLinkAction;

    @Mock SetAsAction setAsAction;

    @Mock ShareImageAction shareImageAction;

    @Mock ShareImageLinkAction shareImageLinkAction;

    @Mock Context context;

    @Mock Image image;

    @Mock UiHandler uiHandler;

    @Mock Drawable mockDrawable;

    @Mock File mockFile;

    private MockShareListener listener;

    private MockImageDownloader mockImageLoader;

    private ShareDelegateFactory shareDelegateFactory;

    @Override
    public void init() {
        super.init();
        MockUiHandlerFactory mockUiHandlerFactory = new MockUiHandlerFactory();
        mockUiHandlerFactory.setMockUiHandler(uiHandler);

        mockUrlShortener = new MockUrlShortener();

        listener = new MockShareListener();
        Mockito.when(image.getTitle()).thenReturn(IMAGE_TITLE);
        Mockito.when(image.getShareUrl()).thenReturn(IMAGE_SHARE_URL);

        Mockito.when(deviceResolution.getPortraitWidth()).thenReturn(DEVICE_WIDTH);

        // Creating a handler which executes runnables immediately
        Mockito.when(uiHandler.post(Mockito.any(Runnable.class))).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Runnable msg = invocation.getArgument(0);
                        msg.run();
                        return null;
                    }
                }
        );

        MockShareActionFactory mockShareActionFactory = new MockShareActionFactory();
        mockShareActionFactory.setMockActions(
                copyLinkAction,
                setAsAction,
                shareImageAction,
                shareImageLinkAction
        );

        mockImageLoader = new MockImageDownloader();
        mockImageLoader.setMockDrawable(mockDrawable);
        mockImageLoader.setMockFile(mockFile);

        shareDelegateFactory = new ShareDelegateFactory(
                mockUrlShortener,
                deviceResolution,
                mockShareActionFactory,
                mockUiHandlerFactory,
                mockImageLoader
        );
    }

    @Test
    public void test_copy_link_delegate() {
        CopyLinkDelegate copyLinkShareDelegate =
                (CopyLinkDelegate) shareDelegateFactory.create(Mode.COPY_LINK);
        copyLinkShareDelegate.share(context, image, listener);

        Mockito.verify(copyLinkAction).copy(context, image.getShareUrl());
        Assert.assertTrue(listener.isShareComplete);
    }

    @Test
    public void test_set_as_delegate() {
        SetAsDelegate setAsDelegate = (SetAsDelegate) shareDelegateFactory.create(Mode.SET_AS);
        setAsDelegate.share(context, image, listener);

        Mockito.verify(image).getUrl(DEVICE_WIDTH * 2);
        Mockito.verify(setAsAction).setAs(context, mockFile);
        Assert.assertTrue(listener.isShareComplete);
    }

    @Test
    public void test_share_image_link_delegate() {
        ShareImageLinkDelegate shareImageLinkDelegate =
                (ShareImageLinkDelegate) shareDelegateFactory.create(Mode.LINK);
        shareImageLinkDelegate.share(context, image, listener);

        Mockito.verify(shareImageLinkAction).shareImageLink(context, IMAGE_TITLE, IMAGE_SHARE_URL);
        Assert.assertTrue(listener.isShareComplete);
    }

    @Test
    public void test_share_image_link_delegate_with_url_shortening() {
        Url longImageShareUrl = new Url("https://someplace.com/image/sidjsdajssidjaosidjaosijfosijfisfdsij.jpg");
        final Url shortenedUrl = new Url("https://spl.com/i/sda.jpg");

        mockUrlShortener.setMockShortUrl(shortenedUrl);

        Mockito.when(image.getShareUrl()).thenReturn(longImageShareUrl);

        ShareImageLinkDelegate shareImageLinkDelegate =
                (ShareImageLinkDelegate) shareDelegateFactory.create(Mode.LINK);
        shareImageLinkDelegate.share(context, image, listener);

        Mockito.verify(shareImageLinkAction).shareImageLink(context, IMAGE_TITLE, shortenedUrl);
        Assert.assertTrue(listener.isShareComplete);
    }

    @Test
    public void test_share_image_delegate() {
        ShareImageDelegate shareImageDelegate =
                (ShareImageDelegate) shareDelegateFactory.create(Mode.PHOTO);
        shareImageDelegate.share(context, image, listener);

        Mockito.verify(shareImageAction).shareImage(context, image, mockFile);
        Assert.assertTrue(listener.isShareComplete);
    }

    private class MockShareListener implements ShareDelegate.Listener {

        boolean isShareComplete;

        @Override
        public void onShareComplete() {
            isShareComplete = true;
        }

        @Override
        public void onShareFailed() {

        }
    }
}
