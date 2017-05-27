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

package com.aashreys.walls.application.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import com.aashreys.maestro.ViewModel;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.DeviceInfo;
import com.aashreys.walls.domain.device.ResourceProvider;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImageInfoService;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.share.ShareDelegate;
import com.aashreys.walls.domain.share.ShareDelegateFactory;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.application.helpers.ImageDownloader;
import com.aashreys.walls.application.helpers.ImageInfoBuilder;
import com.aashreys.walls.application.views.InfoView;
import com.aashreys.walls.utils.LogWrapper;
import com.bumptech.glide.Priority;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 15/04/17.
 */

public class ImageDetailActivityModel implements ViewModel {

    private static final String TAG = ImageDetailActivityModel.class.getSimpleName();

    private final ImageInfoBuilder imageInfoBuilder;

    private final ShareDelegateFactory shareDelegateFactory;

    private final FavoriteImageRepository favoriteImageRepository;

    private final ImageInfoService imageInfoService;

    private final DeviceInfo deviceInfo;

    private final ImageDownloader imageDownloader;

    private final ResourceProvider resourceProvider;

    private ImageView imageView;

    private EventListener eventListener;

    private Image image;

    private ShareDelegate.Listener imageShareListener;

    private ShareDelegate shareImageDelegate, shareLinkDelegate, copyLinkDelegate,
            setAsShareDelegate;

    private boolean isImageLoaded, isImageInfoLoaded, isFavorite;

    @Inject
    ImageDetailActivityModel(
            ImageInfoBuilder imageInfoBuilder,
            ShareDelegateFactory shareDelegateFactory,
            FavoriteImageRepository favoriteImageRepository,
            ImageInfoService imageInfoService,
            DeviceInfo deviceInfo,
            ImageDownloader imageDownloader,
            ResourceProvider resourceProvider
    ) {
        this.imageInfoBuilder = imageInfoBuilder;
        this.shareDelegateFactory = shareDelegateFactory;
        this.favoriteImageRepository = favoriteImageRepository;
        this.imageInfoService = imageInfoService;
        this.deviceInfo = deviceInfo;
        this.imageDownloader = imageDownloader;
        this.resourceProvider = resourceProvider;
        createShareDelegates();
    }

    void setImage(Image image) {
        this.image = image;
        isFavorite = favoriteImageRepository.isFavorite(image);
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @DimenRes
    int getInfoViewHeightRes() {
        return R.dimen.height_small;
    }

    private void createShareDelegates() {
        shareImageDelegate = shareDelegateFactory.create(ShareDelegate.Mode.PHOTO);
        shareLinkDelegate = shareDelegateFactory.create(ShareDelegate.Mode.LINK);
        copyLinkDelegate = shareDelegateFactory.create(ShareDelegate.Mode.COPY_LINK);
        setAsShareDelegate = shareDelegateFactory.create(ShareDelegate.Mode.SET_AS);
        imageShareListener = new ShareDelegate.Listener() {
            @Override
            public void onShareComplete() {
                if (eventListener != null) {
                    eventListener.onImageShareFinished();
                }
            }

            @Override
            public void onShareFailed() {
                if (eventListener != null) {
                    eventListener.onImageShareFinished();
                }
            }
        };
    }

    int getFavoriteButtonIcon() {
        return isFavorite ?
                R.drawable.ic_favorite_black_24dp :
                R.drawable.ic_favorite_border_black_24dp;
    }

    void onFavoriteButtonClicked() {
        if (isFavorite) {
            favoriteImageRepository.unfavorite(image);
        } else {
            favoriteImageRepository.favorite(image);
        }
        isFavorite = !isFavorite;
        if (eventListener != null) {
            eventListener.onFavoriteStateChanged();
        }
    }

    void onSetAsButtonClicked(Context context) {
        if (eventListener != null) {
            eventListener.onSetAsShareStarted();
        }
        setAsShareDelegate.share(
                context,
                image,
                new ShareDelegate.Listener() {
                    @Override
                    public void onShareComplete() {
                        if (eventListener != null) {
                            eventListener.onSetAsShareFinished();
                        }
                    }

                    @Override
                    public void onShareFailed() {
                        if (eventListener != null) {
                            eventListener.onSetAsShareFinished();
                        }
                    }
                }
        );
    }

    void onActivityDestroyed() {
        this.imageView = null;
        this.eventListener = null;
        cancelShareDelegates();
    }

    private void cancelShareDelegates() {
        shareImageDelegate.cancel();
        shareLinkDelegate.cancel();
        copyLinkDelegate.cancel();
        setAsShareDelegate.cancel();
    }

    void onActivityReady(Context context) {
        downloadImage(context);
        downloadImageInfo();
    }

    private void downloadImageInfo() {
        imageInfoService.addInfo(image, new ImageInfoService.Listener() {
            @Override
            public void onComplete(Image imageWithProperties) {
                isImageInfoLoaded = true;
                if (eventListener != null) {
                    eventListener.onImageInfoDownloaded();
                }
                if (isViewReady() && eventListener != null) {
                    eventListener.onViewReady();
                }
            }
        });
    }

    private void downloadImage(Context context) {
        imageDownloader.asDrawable(
                context,
                image.getUrl(deviceInfo.getDeviceResolution().getWidth()),
                Priority.IMMEDIATE,
                imageView,
                new ImageDownloader.Listener<Drawable>() {
                    @Override
                    public void onComplete(Drawable result) {
                        isImageLoaded = true;
                        if (eventListener != null) {
                            eventListener.onImageDownloaded(result);
                        }
                        if (isViewReady() && eventListener != null) {
                            eventListener.onViewReady();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        LogWrapper.i(TAG, "Error loading image, finishing", e);
                        if (eventListener != null) {
                            eventListener.onImageDownloadFailed();
                        }
                    }
                }
        );
    }

    private boolean isViewReady() {
        return isImageLoaded && isImageInfoLoaded;
    }

    String getImageTitle() {
        return image.getTitle() != null ? image.getTitle().value() : null;
    }

    @DimenRes
    int getSubtitleTextLeftMargin() {
        return R.dimen.spacing_xl;
    }

    private String getUserLink() {
        String userLink = null;
        if (getUserPortfolioUrl() != null) {
            userLink = getUserPortfolioUrl();
        } else if (getUserProfileUrl() != null) {
            userLink = getUserProfileUrl();
        }
        return userLink;
    }

    private String getUserProfileUrl() {
        String userProfileUrl = null;
        if (image.getUser() != null && image.getUser().getProfileUrl() != null &&
                image.getUser().getProfileUrl().isValid()) {
            userProfileUrl = image.getUser().getProfileUrl().value();
        }
        return userProfileUrl;
    }

    private String getUserPortfolioUrl() {
        String userPortfolioUrl = null;
        if (image.getUser() != null && image.getUser().getProfileUrl() != null &&
                image.getUser().getProfileUrl().isValid()) {
            userPortfolioUrl = image.getUser().getProfileUrl().value();
        }
        return userPortfolioUrl;
    }

    CharSequence getImageSubtitle() {
        final User user = image.getUser();
        String serviceName = image.getService().getName().value();
        String metaString;
        SpannableString spannableString;
        if (user != null && user.getName() != null && user.getName().value() != null) {
            String userName = user.getName().value();
            metaString = resourceProvider.getString(
                    R.string.title_photo_by,
                    user.getName().value(),
                    image.getService().getName().value()
            );
            spannableString = new SpannableString(metaString);

            if (getUserLink() != null) {
                int indexUserNameStart = metaString.indexOf(userName);
                int indexUserNameEnd = indexUserNameStart + userName.length();
                spannableString.setSpan(
                        new ClickableSpan() {
                            @Override
                            public void onClick(android.view.View widget) {
                                if (eventListener != null) {
                                    eventListener.onUserNameClicked(getUserLink());
                                }
                            }
                        },
                        indexUserNameStart,
                        indexUserNameEnd,
                        0
                );
            }
        } else {
            metaString = resourceProvider.getString(R.string.title_photo_by_only_service_name, serviceName);
            spannableString = new SpannableString(metaString);
        }

        if (image.getService().getUrl() != null && image.getService().getUrl().isValid()) {
            final String serviceUrl = image.getService().getUrl().value();
            int indexServiceNameStart = metaString.indexOf(serviceName);
            int indexServiceNameEnd = indexServiceNameStart + serviceName.length();
            spannableString.setSpan(
                    new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (eventListener != null) {
                                eventListener.onServiceNameClicked(serviceUrl);
                            }

                        }
                    },
                    indexServiceNameStart,
                    indexServiceNameEnd,
                    0
            );
        }
        return spannableString;
    }

    List<InfoView.Info> getImageInfoList() {
        return imageInfoBuilder.buildInfoList(image);
    }

    int getImageInfoColumnCount() {
        return deviceInfo.getNumberOfImageInfoColumns();
    }

    void onShareImageClicked(Context context) {
        notifyImageShareStarted();
        shareImageDelegate.share(context, image, imageShareListener);
    }

    void onShareImageLinkClicked(Context context) {
        notifyImageShareStarted();
        shareLinkDelegate.share(context, image, imageShareListener);
    }

    void onCopyImageLinkClicked(Context context) {
        notifyImageShareStarted();
        copyLinkDelegate.share(context, image, imageShareListener);
    }

    private void notifyImageShareStarted() {
        if (eventListener != null) {
            eventListener.onImageShareStarted();
        }
    }

    interface EventListener {

        void onFavoriteStateChanged();

        void onSetAsShareStarted();

        void onSetAsShareFinished();

        void onViewReady();

        void onImageDownloaded(Drawable image);

        void onImageInfoDownloaded();

        void onImageDownloadFailed();

        void onUserNameClicked(String userUrl);

        void onServiceNameClicked(String serviceUrl);

        void onImageShareStarted();

        void onImageShareFinished();
    }

}
