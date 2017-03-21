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

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.ui.adapters.ImageStreamAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.aashreys.walls.ui.views.LoadingView.ViewMode.ERROR;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.FAVORITE;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.LOADING;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.NOT_LOADING;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.NO_INTERNET;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.END_OF_COLLECTION;
import static com.aashreys.walls.ui.views.LoadingView.ViewMode.SLOW_INTERNET;

/**
 * Created by aashreys on 10/03/17.
 */

public class LoadingView extends LinearLayout {

    private static final int DELAY_BUTTON_FADE_IN = 7000;

    private ProgressBar progressBar;

    private ImageView infoImage;

    private TextView infoText;

    private Button actionButton;

    @ViewMode
    private int currentMode = -1;

    private ImageStreamAdapter.LoadingCallback loadingCallback;

    public LoadingView(Context context) {
        super(context);
        _init(context);
    }

    public LoadingView(
            Context context,
            @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        _init(context);
    }

    public LoadingView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context);
    }

    private void _init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_view_loading, this, true);

        progressBar = (ProgressBar) findViewById(R.id.progress_loading);
        infoImage = (ImageView) findViewById(R.id.image_loading);
        infoText = (TextView) findViewById(R.id.text_loading);
        actionButton = (Button) findViewById(R.id.button_action);
        resetUiState();
    }

    public void setViewMode(@ViewMode int mode) {
        if (currentMode != mode) {
            currentMode = mode;
            switch (mode) {

                case NOT_LOADING:
                    displayNotLoadingState();
                    break;

                case LOADING:
                    displayLoadingState();
                    break;

                case ERROR:
                    displayErrorState();
                    break;

                case SLOW_INTERNET:
                    displaySlowInternetState();
                    break;

                case NO_INTERNET:
                    displayNoInternetState();
                    break;

                case FAVORITE:
                    displayFavoriteHintState();
                    break;

                case END_OF_COLLECTION:
                    displayReachedEndState();
            }
        }
    }

    private void displayReachedEndState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();
        infoImage.setVisibility(VISIBLE);
        infoImage.setImageResource(R.drawable.ic_info_outline_black_24dp);
        infoText.setVisibility(VISIBLE);
        infoText.setText(R.string.hint_end_of_collection);
    }

    private void resetUiState() {
        setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        infoImage.setVisibility(GONE);
        infoImage.setImageResource(0);
        infoText.setVisibility(GONE);
        infoText.setText(null);
        actionButton.setAlpha(1f);
        actionButton.setVisibility(GONE);
        actionButton.setOnClickListener(null);
        actionButton.animate().cancel();
    }

    private void displayNotLoadingState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();
        setVisibility(GONE);
    }

    private void displayLoadingState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();

        progressBar.setVisibility(VISIBLE);
    }

    private void displayErrorState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();

        infoImage.setVisibility(VISIBLE);
        infoImage.setImageResource(R.drawable.ic_error_outline_black_24dp);

        infoText.setVisibility(VISIBLE);
        infoText.setText(R.string.error_generic);

        actionButton.setVisibility(VISIBLE);
        actionButton.setText(R.string.action_try_again);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingCallback != null) {
                    loadingCallback.onLoadRequested();
                }
            }
        });
    }

    private void displayNoInternetState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();

        infoImage.setVisibility(VISIBLE);
        infoImage.setImageResource(R.drawable.ic_connectivity_error_black_24dp);

        infoText.setVisibility(VISIBLE);
        infoText.setText(R.string.error_no_connectivity);

        actionButton.setVisibility(VISIBLE);
        actionButton.setText(R.string.action_try_again);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingCallback != null) {
                    loadingCallback.onLoadRequested();
                }
            }
        });
    }

    private void displaySlowInternetState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();

        progressBar.setVisibility(VISIBLE);

        infoText.setVisibility(VISIBLE);
        infoText.setText(R.string.error_slow_connectivity);

        actionButton.setText(R.string.action_try_again);
        actionButton.setAlpha(0f);
        actionButton.animate().alpha(1f)
                .setStartDelay(DELAY_BUTTON_FADE_IN)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        actionButton.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        actionButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (loadingCallback != null) {
                                    loadingCallback.onLoadRequested();
                                }
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        actionButton.setVisibility(INVISIBLE);
                        actionButton.setAlpha(1f);
                        actionButton.setOnClickListener(null);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void displayFavoriteHintState() {
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        resetUiState();

        infoImage.setVisibility(VISIBLE);
        infoImage.setImageResource(R.drawable.ic_favorite_black_24dp);

        infoText.setVisibility(VISIBLE);
        infoText.setText(R.string.hint_add_favorites);
    }

    public void setLoadingCallback(ImageStreamAdapter.LoadingCallback callback) {
        this.loadingCallback = callback;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            LOADING,
            ERROR,
            NO_INTERNET,
            SLOW_INTERNET,
            NOT_LOADING,
            FAVORITE,
            END_OF_COLLECTION
    })
    public @interface ViewMode {

        int LOADING = 0;

        int ERROR = 1;

        int NO_INTERNET = 2;

        int NOT_LOADING = 3;

        int SLOW_INTERNET = 4;

        int FAVORITE = 5;

        int END_OF_COLLECTION = 6;

    }

}
