package com.aashreys.walls.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.aashreys.walls.LogWrapper;
import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.ImagePropertiesService;
import com.aashreys.walls.domain.display.images.utils.ImageCache;
import com.aashreys.walls.domain.share.Sharer;
import com.aashreys.walls.domain.share.SharerFactory;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.domain.values.Value;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.helpers.ChromeTabHelper;
import com.aashreys.walls.ui.views.InformationRowView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by aashreys on 21/02/17.
 */

public class ImageDetailActivity extends BaseActivity {

    private static final String TAG = ImageDetailActivity.class.getSimpleName();

    private static final String ARG_IMAGE_ID = "arg_image_id";

    private static final String ARG_IMAGE_SERVICE_NAME = "arg_image_service_name";

    @Inject SharerFactory sharerFactory;

    @Inject FavoriteImageRepository favoriteImageRepository;

    @Inject ImagePropertiesService imagePropertiesService;

    @Inject ImageCache imageCache;

    private Sharer imageSharer;

    private Sharer setAsSharer;

    @Nullable
    private Image image;

    private ImageView imageView;

    private ImageButton favoriteButton, setAsButton, shareButton;

    private LinearLayout imageInfoParent;

    private boolean isImageLoaded, isPropertiesLoaded;

    private ProgressBar progressBar;

    private ScrollView parentView;

    public static Intent createLaunchIntent(Context context, Image image) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ARG_IMAGE_ID, image.getId().value());
        intent.putExtra(ARG_IMAGE_SERVICE_NAME, image.getProperties().serviceName.value());
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
            parentView = (ScrollView) findViewById(R.id.parent);
            imageView = (ImageView) findViewById(R.id.image);
            imageInfoParent = (LinearLayout) findViewById(R.id.parent_image_info);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);

            final boolean isFavorite = favoriteImageRepository.isFavorite(image);
            favoriteButton = (ImageButton) findViewById(R.id.button_favorite);
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

            shareButton = (ImageButton) findViewById(R.id.button_share);
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

            setAsButton = (ImageButton) findViewById(R.id.button_set_as);
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


            Glide.with(this)
                    .load(image.getUrl(Image.UrlType.IMAGE_DETAIL).value())
                    .priority(Priority.IMMEDIATE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(
                                Exception e,
                                String model,
                                Target<GlideDrawable> target,
                                boolean isFirstResource
                        ) {
                            finish();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(
                                GlideDrawable resource,
                                String model,
                                Target<GlideDrawable> target,
                                boolean isFromMemoryCache,
                                boolean isFirstResource
                        ) {
                            isImageLoaded = true;
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
                                    Log.d(
                                            TAG,
                                            String.format(
                                                    "View dimensions: %d %d %d %d",
                                                    left,
                                                    top,
                                                    right,
                                                    bottom
                                            )
                                    );
                                    imageView.removeOnLayoutChangeListener(this);
                                    showView();
                                }
                            });
                            return false;
                        }
                    })
                    .into(imageView);

            imagePropertiesService.addProperties(image, new ImagePropertiesService.Listener() {
                @Override
                public void onComplete(Image imageWithProperties) {
                    isPropertiesLoaded = true;
                    addSourceInfo(image, imageInfoParent);
                    addPropertiesInfo(image, imageInfoParent);
                    showView();
                }
            });
        } else {
            LogWrapper.i(TAG, "No image passed, finishing.");
            finish();
        }
    }

    private void showView() {
        if (isImageLoaded) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            if (isPropertiesLoaded) {
                for (int i = 0; i < imageInfoParent.getChildCount(); i++) {
                    imageInfoParent.getChildAt(i).setVisibility(View.VISIBLE);
                }
                favoriteButton.setVisibility(View.VISIBLE);
                setAsButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
            }
            TransitionManager.beginDelayedTransition(parentView, new Fade());
        }
    }

    private void addSourceInfo(Image image, ViewGroup parent) {
        int sourceSubheadIndex = parent.indexOfChild(parent.findViewById(R.id.subhead_source));
        if (sourceSubheadIndex != -1) {
            int currentIndex = sourceSubheadIndex + 1;
            Name userRealName = image.getProperties().userRealName;
            Url userProfileUrl = image.getProperties().userProfileUrl;
            if (userRealName != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_photographer,
                        userRealName.value(),
                        Value.getNullableValue(userProfileUrl),
                        View.INVISIBLE
                ), currentIndex);
                currentIndex += 1;
            }
            Url userPortfolioUrl = image.getProperties().userPortfolioUrl;
            if (userPortfolioUrl != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_portfolio,
                        userPortfolioUrl.value(),
                        userPortfolioUrl.value(),
                        View.INVISIBLE
                ), currentIndex);
                currentIndex += 1;
            }
            Name serviceName = image.getProperties().serviceName;
            Url serviceUrl = image.getProperties().serviceUrl;
            if (serviceName != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_service,
                        serviceName.value(),
                        Value.getNullableValue(serviceUrl),
                        View.INVISIBLE
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
            Pixel resX, resY;
            resX = image.getProperties().resX;
            resY = image.getProperties().resY;
            if (resX != null && resY != null) {
                //noinspection ConstantConditions
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_resolution,
                        formatResolution(resX.value(), resY.value()),
                        null,
                        View.INVISIBLE
                ), currentIndex);
                currentIndex += 1;
            }
            Date createdAt = image.getProperties().createdAt;
            if (createdAt != null) {
                parent.addView(getInfoWithLink(
                        parent,
                        R.string.title_info_captured_on,
                        SimpleDateFormat.getDateInstance().format(createdAt),
                        null,
                        View.INVISIBLE
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
            @Nullable final String url,
            int visibility
    ) {
        //noinspection ResourceType
        InformationRowView informationRowView = getInfoView(
                parent,
                key,
                value,
                url != null ? R.drawable.ic_open_in_browser_black_24dp : null,
                url != null ? new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChromeTabHelper.openUrl(ImageDetailActivity.this, url);
                    }
                } : null
        );
        informationRowView.setVisibility(visibility);
        return informationRowView;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (image != null && isFinishing()) {
            imageCache.remove(image);
        }
    }
}
