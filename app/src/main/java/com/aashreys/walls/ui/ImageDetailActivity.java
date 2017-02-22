package com.aashreys.walls.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aashreys.walls.R;
import com.aashreys.walls.Utils;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.share.Sharer;
import com.aashreys.walls.domain.share.SharerFactory;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.views.InformationRowView;
import com.bumptech.glide.Priority;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

/**
 * Created by aashreys on 21/02/17.
 */

public class ImageDetailActivity extends BaseActivity {

    private static final String TAG = ImageDetailActivity.class.getSimpleName();

    private static final String ARG_IMAGE = "image";

    @Inject SharerFactory sharerFactory;

    @Inject FavoriteImageRepository favoriteImageRepository;

    private Sharer imageSharer;

    private Sharer setAsSharer;

    public static Intent createLaunchIntent(Context context, Image image) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ARG_IMAGE, image);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_detail);
        getUiComponent().inject(this);
        final Image image = getIntent().getParcelableExtra(ARG_IMAGE);
        if (image == null) {
            throw new RuntimeException("Must pass an image in intent");
        }

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Utils.displayImageAsync(this, image.regularImageUrl(), imageView, Priority.IMMEDIATE);

        LinearLayout imageInfoParent = (LinearLayout) findViewById(R.id.parent_image_info);
        if (imageInfoParent != null) {
            addSourceInfo(image, imageInfoParent);
            addPropertiesInfo(image, imageInfoParent);
        }

        final boolean isFavorite = favoriteImageRepository.isFavorite(image);
        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.button_favorite);
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

        final ImageButton shareButton = (ImageButton) findViewById(R.id.button_share);
        final ProgressBar shareProgress = (ProgressBar) findViewById(R.id.progress_share);
        imageSharer = sharerFactory.create(Sharer.ShareMode.ONLY_URL);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareButton.setVisibility(View.INVISIBLE);
                shareProgress.setVisibility(View.VISIBLE);
                imageSharer.share(
                        ImageDetailActivity.this,
                        image,
                        new Sharer.Listener() {
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
                        }
                );
            }
        });

        final ImageButton setAsButton = (ImageButton) findViewById(R.id.button_set_as);
        final ProgressBar setAsProgress = (ProgressBar) findViewById(R.id.progress_set_as);
        setAsSharer = sharerFactory.create(Sharer.ShareMode.SET_AS);
        setAsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAsButton.setVisibility(View.INVISIBLE);
                setAsProgress.setVisibility(View.VISIBLE);
                setAsSharer.share(
                        ImageDetailActivity.this,
                        image,
                        new Sharer.Listener() {
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

    }

    private void addSourceInfo(Image image, ViewGroup parent) {
        int sourceSubheadIndex = parent.indexOfChild(parent.findViewById(R.id.subhead_source));
        if (sourceSubheadIndex != -1) {
            int currentIndex = sourceSubheadIndex + 1;
            if (image.userRealName() != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_photographer,
                        image.userRealName().value(),
                        image.userProfileUrl().value()
                ), currentIndex);
                currentIndex += 1;
            }
            if (image.userPortfolioUrl() != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_portfolio,
                        image.userPortfolioUrl().value(),
                        image.userPortfolioUrl().value()
                ), currentIndex);
                currentIndex += 1;
            }
            if (image.serviceName() != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_service,
                        image.serviceName().value(),
                        image.serviceUrl().value()
                ), currentIndex);
                currentIndex += 1;
            }
        }
    }

    private void addPropertiesInfo(Image image, ViewGroup parent) {
        int propertiesSubheadIndex =
                parent.indexOfChild(parent.findViewById(R.id.subhead_properties));
        if (propertiesSubheadIndex != -1) {
            int currentIndex = propertiesSubheadIndex + 1;
            if (image.resX() != null && image.resY() != null) {
                //noinspection ConstantConditions
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_resolution,
                        formatResolution(image.resX().value(), image.resY().value()),
                        null
                ), currentIndex);
                currentIndex += 1;
            }
            if (image.createdAt() != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_captured_on,
                        SimpleDateFormat.getDateInstance().format(image.createdAt()),
                        null
                ), currentIndex);
                currentIndex += 1;
            }
        }
    }

    private String formatResolution(int resX, int resY) {
        return getString(R.string.resolution_formatted_string, resX, resY);
    }

    private InformationRowView getInfoView(
            ViewGroup parent,
            @StringRes int key,
            String value,
            @Nullable @DrawableRes Integer actionDrawable,
            @Nullable View.OnClickListener listener
    ) {
        InformationRowView
                view = (InformationRowView) LayoutInflater.from(this)
                .inflate(
                        R.layout.item_image_information_view,
                        parent,
                        false
                );
        view.setKey(key);
        view.setValue(value);
        view.setAction(actionDrawable, listener);
        return view;
    }

    private InformationRowView getInfoWithLink(
            ViewGroup parent,
            @StringRes int key,
            String value,
            @Nullable final String url
    ) {
        //noinspection ResourceType
        return getInfoView(
                parent,
                key,
                value,
                url != null ? R.drawable.ic_open_in_browser_black_24dp : null,
                url != null ? new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.openUrl(ImageDetailActivity.this, url);
                    }
                } : null
        );
    }
}
