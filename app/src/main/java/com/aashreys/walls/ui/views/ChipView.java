package com.aashreys.walls.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;

/**
 * Created by aashreys on 04/02/17.
 */
public class ChipView extends TextView {

    @Nullable
    private OnSelectedListener listener;

    private boolean isChecked;

    public ChipView(Context context) {
        super(context);
        _init(context);
    }

    private void _init(Context context) {
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.chip_padding_vertical);
        int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen
                .chip_padding_horizontal);
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        setBackgroundResource(R.drawable.chip_background_light);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context);
    }

    public void setCollection(final Collection collection) {
        setText(collection.getName().value().toLowerCase());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked()) {
                    notifyListener(null, null);
                    setChecked(false);
                } else {
                    notifyListener(ChipView.this, collection);
                    setChecked(true);
                }
            }
        });
    }

    private void notifyListener(ChipView chipView, Collection collection) {
        if (listener != null) {
            listener.onChipViewSelected(chipView, collection);
        }
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            setBackgroundResource(R.drawable.chip_background_dark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextColor(getResources().getColor(R.color.white, null));
            } else {
                setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            setBackgroundResource(R.drawable.chip_background_light);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextColor(getResources().getColor(R.color.black, null));
            } else {
                setTextColor(getResources().getColor(R.color.black));
            }
        }
        this.isChecked = isChecked;
    }

    public void resetState() {
        setText(null);
    }

    public interface OnSelectedListener {

        void onChipViewSelected(@Nullable ChipView view, @Nullable Collection collection);

    }
}
