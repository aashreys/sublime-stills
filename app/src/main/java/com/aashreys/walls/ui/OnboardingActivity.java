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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.ui.views.ChipView;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

public class OnboardingActivity extends BaseActivity<OnboardingActivityModel> implements
        OnboardingActivityModel.EventListener {

    private ProgressBar progressBar;

    private FlowLayout collectionsParent;

    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getViewModel().setEventListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        collectionsParent = (FlowLayout) findViewById(R.id.parent_collections);
        continueButton = (Button) findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().onContinueButtonClicked();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        collectionsParent.setVisibility(View.INVISIBLE);

        getViewModel().onActivityReady();
    }

    @Override
    protected OnboardingActivityModel createViewModel() {
        OnboardingActivityModel viewModel = getUiComponent().createOnboardingActivityModel();
        viewModel.onInjectionComplete();
        return viewModel;
    }

    @Override
    public void onSearchComplete(List<Collection> collectionList) {
        collectionsParent.removeAllViews();
        if (collectionList != null && collectionList.size() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
            collectionsParent.setVisibility(View.VISIBLE);
            int chipViewHeight =
                    getResources().getDimensionPixelSize(getViewModel().getChipViewHeight());
            int chipViewMargin =
                    getResources().getDimensionPixelSize(getViewModel().getChipViewMargin());
            for (Collection collection : collectionList) {
                if (collection.getName().value().length() > 3) {
                    ChipView chipView = new ChipView(this);
                    chipView.setCollection(collection);
                    chipView.setOnCheckedListener(getViewModel().getChipViewOnCheckedListener());
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            chipViewHeight
                    );
                    params.setMargins(
                            chipViewMargin,
                            chipViewMargin,
                            chipViewMargin,
                            chipViewMargin
                    );
                    collectionsParent.addView(chipView, params);
                    chipView.setChecked(getViewModel().isCollectionChecked(collection));
                }
            }
        }
    }

    @Override
    public void onOnboardingComplete() {
        finish();
        startActivity(new Intent(this, StreamActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
