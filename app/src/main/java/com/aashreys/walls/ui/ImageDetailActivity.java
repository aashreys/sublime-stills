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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.ShareDelegate;
import com.aashreys.walls.ui.helpers.ChromeTabHelper;
import com.aashreys.walls.ui.views.InfoView;
import com.aashreys.walls.utils.LogWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aashreys on 21/02/17.
 */

public class ImageDetailActivity extends BaseActivity<ImageDetailActivityModel> implements
        ImageDetailActivityModel.EventListener {

    private static final String TAG = ImageDetailActivity.class.getSimpleName();

    private static final String ARG_IMAGE = "arg_image";

    private ImageView imageView;

    private ImageButton favoriteButton, setAsButton, shareButton;

    private ProgressBar progressBar, shareProgress;

    private ViewGroup contentParent;

    private TextView titleText, subtitleText;

    private TableLayout infoTable;

    private MenuPopupHelper shareMenuHelper;

    private ProgressBar setAsProgressBar;

    public static Intent createLaunchIntent(Context context, Image image) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ARG_IMAGE, image);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setEventListener(this);
        final Image image = getIntent().getParcelableExtra(ARG_IMAGE);
        if (image != null) {
            getViewModel().setImage(image);
            setContentView(R.layout.activity_image_detail);
            contentParent = (ViewGroup) findViewById(R.id.parent_content);
            imageView = (ImageView) findViewById(R.id.image);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            infoTable = (TableLayout) findViewById(R.id.table_info);

            titleText = (TextView) findViewById(R.id.text_title);
            subtitleText = (TextView) findViewById(R.id.text_subtitle);

            favoriteButton = (ImageButton) findViewById(R.id.button_favorite);
            shareButton = (ImageButton) findViewById(R.id.button_share);
            setAsButton = (ImageButton) findViewById(R.id.button_set_as);

            shareProgress = (ProgressBar) findViewById(R.id.progress_share);
            setAsProgressBar = (ProgressBar) findViewById(R.id.progress_set_as);

            buildShareMenu(shareButton);

            favoriteButton.setImageResource(getViewModel().getFavoriteButtonIcon());
            favoriteButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    getViewModel().onFavoriteButtonClicked();
                }
            });

            shareButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    //noinspection RestrictedApi
                    shareMenuHelper.show();
                }
            });

            setAsButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    getViewModel().onSetAsButtonClicked(ImageDetailActivity.this);
                }
            });

            getViewModel().setImageView(imageView);
            getViewModel().onActivityReady(this);
        } else {
            LogWrapper.i(TAG, "No image passed, finishing.");
            finish();
        }
    }

    @Override
    protected ImageDetailActivityModel createViewModel() {
        ImageDetailActivityModel viewModel = getUiComponent().createImageDetailActivityModel();
        viewModel.onInjectionComplete();
        return viewModel;
    }

    private void displayInfo() {
        int infoColumns = getViewModel().getImageInfoColumnCount();
        List<InfoView.Info> infoList = getViewModel().getImageInfoList();

        TableRow tableRow = null;
        int height = getResources().getDimensionPixelSize(getViewModel().getInfoViewHeightRes());
        for (int i = 0; i < infoList.size(); i++) {
            int columnPosition = i % infoColumns;
            if (columnPosition == 0) {
                tableRow = new TableRow(this);
                infoTable.addView(tableRow);
            }
            InfoView infoView = addInfoView(tableRow, height, columnPosition, infoColumns);
            infoView.setInfo(infoList.get(i));
        }
    }

    private InfoView addInfoView(TableRow row, int height, int columnPosition, int numColumns) {
        InfoView infoView = new InfoView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, height, 1);
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

    private void displayHeader() {
        if (getViewModel().getImageTitle() != null) {
            titleText.setText(getViewModel().getImageTitle());
        } else {
            titleText.setVisibility(android.view.View.GONE);
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) subtitleText.getLayoutParams();
            params.setMargins(
                    params.leftMargin,
                    getResources().getDimensionPixelSize(getViewModel().getSubtitleTextLeftMargin()),
                    params.rightMargin,
                    params.bottomMargin
            );
            subtitleText.setLayoutParams(params);
        }
        subtitleText.setText(getViewModel().getImageSubtitle());
        subtitleText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getFormattedDate(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }

    private String formatResolution(int resX, int resY) {
        return getString(R.string.resolution_formatted_string, resX, resY);
    }

    @SuppressWarnings("RestrictedApi")
    private void buildShareMenu(android.view.View anchor) {
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
        switch (mode) {
            case PHOTO:
                getViewModel().onShareImageClicked(this);
                break;

            case LINK:
                getViewModel().onShareImageLinkClicked(this);
                break;

            case COPY_LINK:
                getViewModel().onCopyImageLinkClicked(this);
                Toast.makeText(this, R.string.confirmation_link_copied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewModel().onActivityDestroyed();
    }

    @Override
    public void onFavoriteStateChanged() {
        favoriteButton.setImageResource(getViewModel().getFavoriteButtonIcon());
    }

    @Override
    public void onSetAsShareStarted() {
        setAsButton.setVisibility(android.view.View.INVISIBLE);
        setAsProgressBar.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void onSetAsShareFinished() {
        setAsProgressBar.setVisibility(android.view.View.INVISIBLE);
        setAsButton.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void onViewReady() {
        TransitionManager.beginDelayedTransition(
                contentParent,
                new Fade().setInterpolator(new AccelerateDecelerateInterpolator())
        );
        progressBar.setVisibility(android.view.View.INVISIBLE);
        contentParent.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void onImageDownloaded(Drawable image) {
        imageView.setImageDrawable(image);
    }

    @Override
    public void onImageInfoDownloaded() {
        displayHeader();
        displayInfo();
    }

    @Override
    public void onImageDownloadFailed() {
        finish();
    }

    @Override
    public void onUserNameClicked(String userUrl) {
        ChromeTabHelper.openUrl(this, userUrl);
    }

    @Override
    public void onServiceNameClicked(String serviceUrl) {
        ChromeTabHelper.openUrl(this, serviceUrl);
    }

    @Override
    public void onImageShareStarted() {
        shareButton.setVisibility(android.view.View.INVISIBLE);
        shareProgress.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void onImageShareFinished() {
        shareProgress.setVisibility(android.view.View.INVISIBLE);
        shareButton.setVisibility(android.view.View.VISIBLE);
    }
}
