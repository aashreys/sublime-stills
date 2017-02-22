package com.aashreys.walls.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashreys.walls.R;

/**
 * Created by aashreys on 24/11/16.
 */

public class InformationRowView extends LinearLayout {

    private TextView key;

    private TextView value;

    private ImageButton actionButton;

    public InformationRowView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    public InformationRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public InformationRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InformationRowView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_item_image_information_row, this, true);
        key = (TextView) findViewById(R.id.text_key);
        value = (TextView) findViewById(R.id.text_value);
        actionButton = (ImageButton) findViewById(R.id.button_action);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InformationRowView,
                0, 0
        );
        try {
            String key = a.getString(R.styleable.InformationRowView_irv_key);
            String value = a.getString(R.styleable.InformationRowView_irv_value);
            setKey(key);
            setValue(value);
        } finally {
            a.recycle();
        }
    }

    public void setKey(String key) {
        this.key.setText(key);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public void setKey(@StringRes int key) {
        setKey(getResources().getString(key));
    }

    public void setValue(@StringRes int value) {
        setKey(getResources().getString(value));
    }

    public void setAction(@DrawableRes Integer drawable, OnClickListener listener) {
        if (drawable != null) {
            actionButton.setVisibility(VISIBLE);
            actionButton.setImageResource(drawable); // so that we can load
            // vector drawables
            if (listener != null) {
                actionButton.setOnClickListener(listener);
            }
        } else {
            actionButton.setVisibility(INVISIBLE);
        }
    }
}
