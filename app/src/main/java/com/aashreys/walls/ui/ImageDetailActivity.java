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

package com.aashreys.walls.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.device.DeviceResolution;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImageInfoService;
import com.aashreys.walls.domain.display.images.metadata.Exif;
import com.aashreys.walls.domain.display.images.metadata.Location;
import com.aashreys.walls.domain.display.images.metadata.Resolution;
import com.aashreys.walls.domain.display.images.metadata.User;
import com.aashreys.walls.domain.display.images.utils.ImageCache;
import com.aashreys.walls.domain.share.ShareDelegate;
import com.aashreys.walls.domain.share.ShareDelegateFactory;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.helpers.ChromeTabHelper;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.views.InfoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aashreys on 21/02/17.
 */

public class ImageDetailActivity extends BaseActivity {

    private static final String TAG = ImageDetailActivity.class.getSimpleName();

    private static final String ARG_IMAGE_ID = "arg_image_id";

    private static final String ARG_IMAGE_SERVICE_NAME = "arg_image_service_name";

    @Inject ShareDelegateFactory shareDelegateFactory;

    @Inject FavoriteImageRepository favoriteImageRepository;

    @Inject ImageInfoService imageInfoService;

    @Inject ImageCache imageCache;

    @Inject DeviceResolution deviceResolution;

    private ShareDelegate shareImageDelegate, shareLinkDelegate, copyLinkDelegate;

    private ShareDelegate setAsShareDelegate;

    @Nullable
    private Image image;

    private ImageView imageView;

    private ImageButton favoriteButton, setAsButton, shareButton;

    private boolean isImageLoaded, isInfoLoaded;

    private ProgressBar progressBar, shareProgress;

    private ViewGroup contentParent;

    private TextView titleText, metaText;

    private TableLayout infoTable;

    private int infoViewHeight;

    private MenuPopupHelper shareMenuHelper;

    public static Intent createLaunchIntent(Context context, Image image) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ARG_IMAGE_ID, image.getId().value());
        intent.putExtra(ARG_IMAGE_SERVICE_NAME, image.getService().getName().value());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUiComponent().inject(this);
        final Image image = getImageFromArgs();
        if (image != null) {
            setContentView(R.layout.activity_image_detail);
            this.image = image;
            contentParent = (ViewGroup) findViewById(R.id.parent_content);
            imageView = (ImageView) findViewById(R.id.image);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            infoTable = (TableLayout) findViewById(R.id.table_info);

            titleText = (TextView) findViewById(R.id.text_title);
            metaText = (TextView) findViewById(R.id.text_meta);

            favoriteButton = (ImageButton) findViewById(R.id.button_favorite);
            shareButton = (ImageButton) findViewById(R.id.button_share);
            setAsButton = (ImageButton) findViewById(R.id.button_set_as);

            shareProgress = (ProgressBar) findViewById(R.id.progress_share);
            final ProgressBar setAsProgress = (ProgressBar) findViewById(R.id.progress_set_as);

            infoViewHeight = getResources().getDimensionPixelSize(R.dimen.height_small);

            buildShareDelegates();
            buildShareMenu(shareButton);

            final boolean isFavorite = favoriteImageRepository.isFavorite(image);
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                boolean isFavorite2 = isFavorite;

                @Override
                public void onClick(View view) {
                    if (isFavorite2) {
                        favoriteImageRepository.unfavorite(image);
                        favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        favoriteImageRepository.favorite(image);
                        favoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                    isFavorite2 = !isFavorite2;
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //noinspection RestrictedApi
                    shareMenuHelper.show();
                }
            });

            setAsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAsButton.setVisibility(View.INVISIBLE);
                    setAsProgress.setVisibility(View.VISIBLE);
                    setAsShareDelegate.share(
                            ImageDetailActivity.this,
                            image,
                            new ShareDelegate.Listener() {
                                @Override
                                public void onShareComplete() {
                                    setAsProgress.setVisibility(View.INVISIBLE);
                                    setAsButton.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onShareFailed() {
                                    setAsProgress.setVisibility(View.INVISIBLE);
                                    setAsButton.setVisibility(View.VISIBLE);
                                }
                            }
                    );
                }
            });

            int width;
            if (UiHelper.isPortrait(this)) {
                width = deviceResolution.getPortraitWidth();
            } else {
                width = deviceResolution.getPortraitHeight();
            }
            Glide.with(this)
                    .load(image.getUrl(width).value())
                    .priority(Priority.IMMEDIATE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(
                                Exception e,
                                String model,
                                Target<GlideDrawable> target,
                                boolean isFirstResource
                        ) {
                            LogWrapper.i(TAG, "Error loading image, finishing", e);
                            finish();
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(
                                GlideDrawable resource,
                                String model,
                                Target<GlideDrawable> target,
                                boolean isFromMemoryCache,
                                boolean isFirstResource
                        ) {

                            imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(
                                        View v,
                                        int left,
                                        int top,
                                        int right,
                                        int bottom,
                                        int oldLeft,
                                        int oldTop,
                                        int oldRight,
                                        int oldBottom
                                ) {
                                    isImageLoaded = true;
                                    imageView.removeOnLayoutChangeListener(this);
                                    showView();
                                }
                            });
                            return false;
                        }
                    })
                    .into(imageView);

            imageInfoService.addInfo(image, new ImageInfoService.Listener() {
                @Override
                public void onComplete(Image imageWithProperties) {
                    displayHeader();
                    displayInfo();
                    isInfoLoaded = true;
                    showView();
                }
            });
        } else {
            LogWrapper.i(TAG, "No image passed, finishing.");
            finish();
        }
    }

    private void displayInfo() {
        int infoColumns = getResources().getInteger(R.integer.image_info_column_count);
        List<Pair<Value, Integer>> infoList = new ArrayList<>();

        Date createdAt = image.getUploadDate();
        if (createdAt != null) {
            putValueIconPair(
                    infoList,
                    new Name(getFormattedDate(createdAt)),
                    R.drawable.ic_date_black_24dp
            );
        }

        Location location = image.getLocation();
        if (location != null) {
            Name locationName;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (location.getName() != null) {
                locationName = location.getName();
            } else if (latitude != 0 || longitude != 0) {
                locationName = new Name(latitude + ", " + longitude);
            } else {
                locationName = new Name(getString(R.string.title_location_elsweyr));
            }
            putValueIconPair(infoList, locationName, R.drawable.ic_location_pin_black_24dp);
        }

        Resolution resolution = image.getResolution();
        if (resolution != null) {
            putValueIconPair(
                    infoList,
                    new Name(formatResolution(
                            resolution.getResX().value(),
                            resolution.getResY().value()
                    )),
                    R.drawable.ic_dimensions_black_24dp
            );
        }

        Exif exif = image.getExif();
        if (exif != null) {
            putValueIconPair(infoList, exif.camera, R.drawable.ic_camera_black_24dp);
            putValueIconPair(infoList, exif.aperture, R.drawable.ic_aperture_black_24dp);
            putValueIconPair(infoList, exif.exposureTime, R.drawable.ic_exposure_time_black_24dp);
            putValueIconPair(infoList, exif.focalLength, R.drawable.ic_focus_black_24dp);
            putValueIconPair(infoList, exif.iso, R.drawable.ic_iso_black_24dp);
        }

        TableRow tableRow = null;
        for (int i = 0; i < infoList.size(); i++) {
            int columnPosition = i % infoColumns;
            if (columnPosition == 0) {
                tableRow = new TableRow(this);
                infoTable.addView(tableRow);
            }
            InfoView infoView = addInfoView(tableRow, columnPosition, infoColumns);
            Pair<Value, Integer> infoPair = infoList.get(i);
            infoView.setInfoWithIcon(infoPair.second, String.valueOf(infoPair.first.value()));
        }
    }

    private InfoView addInfoView(TableRow row, int columnPosition, int numColumns) {
        InfoView infoView = new InfoView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, infoViewHeight, 1);
        int margin = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        if (columnPosition == 0) {
            params.setMargins(0, 0, margin, 0);
        } else if (columnPosition == numColumns - 1) {
            params.setMargins(margin, 0, 0, 0);
        } else {
            params.setMargins(margin, 0, margin, 0);
        }
        row.addView(infoView, params);
        return infoView;

    }

    private void putValueIconPair(List<Pair<Value, Integer>> target, Value value, int icon) {
        if (value != null && value.isValid()) {
            target.add(new Pair<>(value, icon));
        }
    }

    private void showView() {
        if (isImageLoaded && isInfoLoaded) {
            TransitionManager.beginDelayedTransition(contentParent, new Fade());
            progressBar.setVisibility(View.INVISIBLE);
            contentParent.setVisibility(View.VISIBLE);
        }
    }

    private void displayHeader() {
        Name title = image.getTitle();
        Name serviceName = image.getService().getName();
        final Url serviceUrl = image.getService().getUrl();

        Name userName = null;
        final User user = image.getUser();
        if (user != null) {
            if (user.getName() != null) {
                userName = user.getName();
            }
        }

        if (title != null && title.isValid()) {
            titleText.setText(title.value());
        } else {
            titleText.setVisibility(View.GONE);
        }

        if (titleText.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) metaText.getLayoutParams();
            params.setMargins(
                    params.leftMargin,
                    getResources().getDimensionPixelSize(R.dimen.spacing_xl),
                    params.rightMargin,
                    params.bottomMargin
            );
            metaText.setLayoutParams(params);
        }

        String metaString;
        SpannableString spannableString;
        if (userName != null && userName.isValid()) {
            metaString = getString(
                    R.string.title_photo_by,
                    userName.value() + " / " + serviceName.value()
            );
            spannableString = new SpannableString(metaString);

            if ((user.getProfileUrl() != null && user.getProfileUrl().isValid()) ||
                    (user.getPortfolioUrl() != null && user.getPortfolioUrl().isValid())) {
                int indexUserNameStart = metaString.indexOf(userName.value());
                int indexUserNameEnd = indexUserNameStart + userName.value().length();
                spannableString.setSpan(
                        new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                if (user.getPortfolioUrl() != null &&
                                        user.getPortfolioUrl().isValid()) {
                                    ChromeTabHelper.openUrl(
                                            ImageDetailActivity.this,
                                            user.getPortfolioUrl().value()
                                    );
                                } else {
                                    ChromeTabHelper.openUrl(
                                            ImageDetailActivity.this,
                                            user.getProfileUrl().value()
                                    );
                                }
                            }
                        },
                        indexUserNameStart,
                        indexUserNameEnd,
                        0
                );
            }
        } else {
            metaString = getString(R.string.title_photo_by, serviceName.value());
            spannableString = new SpannableString(metaString);
        }

        if (serviceUrl != null && serviceUrl.isValid()) {
            int indexServiceNameStart = metaString.indexOf(serviceName.value());
            int indexServiceNameEnd = indexServiceNameStart + serviceName.value().length();
            spannableString.setSpan(
                    new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ChromeTabHelper.openUrl(
                                    ImageDetailActivity.this,
                                    serviceUrl.value()
                            );
                        }
                    },
                    indexServiceNameStart,
                    indexServiceNameEnd,
                    0
            );
        }
        metaText.setText(spannableString);
        metaText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getFormattedDate(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }

    private String formatResolution(int resX, int resY) {
        return getString(R.string.resolution_formatted_string, resX, resY);
    }

    @Nullable
    private Image getImageFromArgs() {
        Id imageId = new Id(getIntent().getStringExtra(ARG_IMAGE_ID));
        Name serviceName = new Name(getIntent().getStringExtra(ARG_IMAGE_SERVICE_NAME));
        if (imageId.isValid() && serviceName.isValid()) {
            return imageCache.get(serviceName, imageId);
        } else {
            return null;
        }
    }

    private void buildShareDelegates() {
        shareImageDelegate = shareDelegateFactory.create(ShareDelegate.Mode.PHOTO);
        shareLinkDelegate = shareDelegateFactory.create(ShareDelegate.Mode.LINK);
        copyLinkDelegate = shareDelegateFactory.create(ShareDelegate.Mode.COPY_LINK);
        setAsShareDelegate = shareDelegateFactory.create(ShareDelegate.Mode.SET_AS);
    }

    @SuppressWarnings("RestrictedApi")
    private void buildShareMenu(View anchor) {
        PopupMenu shareMenu = new PopupMenu(this, anchor);
        shareMenu.getMenuInflater().inflate(R.menu.menu_image_share, shareMenu.getMenu());
        shareMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_share_photo:
                        handleImageShare(ShareDelegate.Mode.PHOTO);
                        return true;

                    case R.id.menu_share_link:
                        handleImageShare(ShareDelegate.Mode.LINK);
                        return true;

                    case R.id.menu_copy_link:
                        handleImageShare(ShareDelegate.Mode.COPY_LINK);
                        return true;

                }
                return false;
            }
        });
        shareMenuHelper = new MenuPopupHelper(
                this,
                (MenuBuilder) shareMenu.getMenu(),
                anchor
        );
        shareMenuHelper.setForceShowIcon(true);
    }

    private void handleImageShare(ShareDelegate.Mode mode) {
        shareButton.setVisibility(View.INVISIBLE);
        shareProgress.setVisibility(View.VISIBLE);
        ShareDelegate.Listener listener = new ShareDelegate.Listener() {
            @Override
            public void onShareComplete() {
                shareProgress.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onShareFailed() {
                shareProgress.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.VISIBLE);
            }
        };
        switch (mode) {
            case PHOTO:
                shareImageDelegate.share(this, image, listener);
                break;

            case LINK:
                shareLinkDelegate.share(this, image, listener);
                break;

            case COPY_LINK:
                copyLinkDelegate.share(this, image, listener);
                Toast.makeText(this, R.string.confirmation_link_copied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareImageDelegate.cancel();
        shareLinkDelegate.cancel();
        copyLinkDelegate.cancel();
        ;
        setAsShareDelegate.cancel();
        if (image != null && isFinishing()) {
            imageCache.remove(image);
        }
    }
}
