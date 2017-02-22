package com.aashreys.walls.ui;

import android.support.v7.app.AppCompatActivity;

import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.di.UiComponent;

/**
 * Created by aashreys on 21/02/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected final UiComponent getUiComponent() {
        return ((WallsApplication) getApplication()).getApplicationComponent().getUiComponent();
    }

}
