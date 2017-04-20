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

package com.aashreys.walls.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.ui.StreamFragment;
import com.aashreys.walls.ui.views.libs.AspectRatioImageView;

/**
 * Created by aashreys on 09/02/17.
 */

public class StreamImageView extends FrameLayout implements StreamImageViewModel.EventCallback {

    private StreamImageViewModel viewModel;

    private AspectRatioImageView imageView;

    private ImageButton favoriteButton;

    public StreamImageView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    public StreamImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public StreamImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StreamImageView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        viewModel = new StreamImageViewModel();
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(viewModel);
        viewModel.setEventCallback(this);
        LayoutInflater.from(context).inflate(R.layout.layout_item_stream_image, this, true);
        imageView = (AspectRatioImageView) findViewById(R.id.image);
        imageView.setWidthToHeightRatio(viewModel.getWidthToHeightRatio());
        favoriteButton = (ImageButton) findViewById(R.id.button_action);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onImageClicked();
            }
        });
        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFavoriteButtonClicked();
            }
        });
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                viewModel.onViewOnScreen();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                viewModel.onViewOffScreen();
            }
        });
    }

    public void bind(
            StreamFragment fragment,
            final Image image,
            final InteractionCallback callback
    ) {
        viewModel.setInteractionCallback(callback);
        viewModel.setData(fragment, imageView, image);
    }

    @Override
    public void onImageDownloaded(Drawable image) {
        setVisibility(VISIBLE);
        imageView.setAlpha(0f);
        imageView.setImageDrawable(image);
        imageView.animate()
                .alpha(1f)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public void onImageDownloadFailed() {
        setVisibility(GONE);
    }

    @Override
    public void onFavoriteSyncStarted() {
        favoriteButton.setVisibility(INVISIBLE);
    }

    @Override
    public void onImageBackgroundChanged(int color) {
        setVisibility(VISIBLE);
        setBackgroundColor(color);
    }

    @Override
    public void onFavoriteStateChanged() {
        favoriteButton.setVisibility(VISIBLE);
        favoriteButton.setImageResource(viewModel.getFavoriteButtonIconRes());
    }

    public interface InteractionCallback {

        void onImageClicked(Image image);

        void onFavoriteButtonClicked(Image image, boolean isFavorited);

    }
}
