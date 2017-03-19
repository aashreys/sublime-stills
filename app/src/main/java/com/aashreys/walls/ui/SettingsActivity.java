package com.aashreys.walls.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

    @Inject KeyValueStore keyValueStore;

    private TextView resetTipsText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getUiComponent().inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_settings);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resetTipsText = (TextView) findViewById(R.id.text_reset_tips);
        resetTipsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTipsSeen();
            }
        });
    }

    private void clearTipsSeen() {
        keyValueStore.putBoolean(getString(R.string.tag_hint_collection), false);
        keyValueStore.putBoolean(getString(R.string.tag_hint_image_actions), false);
        Snackbar.make(resetTipsText, R.string.confirmation_tips_reset, Snackbar.LENGTH_LONG).show();
    }
}
