package com.aashreys.walls.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.ui.AddCollectionDialog;
import com.aashreys.walls.ui.MainActivity;
import com.aashreys.walls.Utils;

/**
 * Created by aashreys on 04/02/17.
 */
public class CollectionTypeView extends LinearLayout {

    private static final String FRAGMENT_TAG = "tag_add_image_source_fragment";

    private TextView  titleText;
    private ImageView iconImage;

    public CollectionTypeView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        LayoutInflater.from(context)
                .inflate(R.layout.layout_item_collection_type, this, true);
        titleText = (TextView) findViewById(R.id.text_title);
        iconImage = (ImageView) findViewById(R.id.image_icon);
    }

    public CollectionTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public CollectionTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CollectionTypeView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImageSourceType(@Collection.Type String type) {
        switch (type) {
            case Collection.Type.UNSPLASH_COLLECTION:
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddCollectionDialog fragment
                                = new AddCollectionDialog();
                        fragment.show(
                                ((MainActivity) getContext()).getSupportFragmentManager(),
                                FRAGMENT_TAG
                        );
                    }
                });
                break;
        }
        titleText.setText(Utils.getTitleForCollectionType(type));
        iconImage.setImageResource(Utils.getIconForCollectionType(type));
    }
}
