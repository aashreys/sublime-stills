package com.aashreys.walls.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.persistence.KeyValueStore;

import javax.inject.Inject;

/**
 * Created by aashreys on 11/03/17.
 */

public class HintView extends FrameLayout {

    @Inject KeyValueStore keyValueStore;

    private TextView hintText;

    private ImageButton closeButton;

    public HintView(Context context) {
        super(context);
        _init(context, null);
    }

    public HintView(
            Context context,
            @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        _init(context, attrs);
    }

    public HintView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs);
    }

    private void _init(Context context, @Nullable AttributeSet attrs) {
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
        LayoutInflater.from(context).inflate(R.layout.layout_view_hint, this, true);
        int verticalPadding = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        setPadding(0, verticalPadding, 0, verticalPadding);

        final String hintString, seenKeyString;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HintView, 0, 0);
        try {
            hintString = a.getString(R.styleable.HintView_hv_hint);
            seenKeyString = a.getString(R.styleable.HintView_hv_seen_key);
        } finally {
            a.recycle();
        }

        hintText = (TextView) findViewById(R.id.text_hint);
        closeButton = (ImageButton) findViewById(R.id.button_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
                if (seenKeyString != null) {
                    keyValueStore.putBoolean(seenKeyString, true);
                }
            }
        });
        if (hintString != null) {
            hintText.setText(hintString);
        }
        if (seenKeyString != null) {
            if (keyValueStore.getBoolean(seenKeyString, false)) {
                setVisibility(GONE);
            }
        }
    }

}
