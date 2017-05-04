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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aashreys.maestro.ViewIdGenerator;
import com.aashreys.maestro.ViewModel;
import com.aashreys.maestro.ViewModelStore;
import com.aashreys.walls.application.WallsApplication;
import com.aashreys.walls.di.ApplicationComponent;
import com.aashreys.walls.di.UiComponent;
import com.aashreys.walls.application.helpers.UiHelper;

/**
 * Created by aashreys on 21/02/17.
 */

public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity {

    private String ARG_VIEW_ID = "arg_view_id";

    private String viewId;

    private VM viewModel;

    private ViewModelStore viewModelStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelStore = getApplicationComponent().viewModelStore();
        setViewModel((VM) viewModelStore.get(viewId));
        if (viewModel == null) {
            viewId = new ViewIdGenerator().generateId(this);
            setViewModel(createViewModel());
        }
    }

    protected final UiComponent getUiComponent() {
        return UiHelper.getUiComponent(this);
    }

    protected final ApplicationComponent getApplicationComponent() {
        return ((WallsApplication) getApplication()).getApplicationComponent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_VIEW_ID, viewId);
    }

    private void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    protected final VM getViewModel() {
        return this.viewModel;
    }

    /* Subclasses to override this to create their ViewModels */
    protected abstract VM createViewModel();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            viewModelStore.remove(viewId);
        } else {
            viewModelStore.put(viewId, viewModel);
        }
    }
}
