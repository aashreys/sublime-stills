package com.aashreys.walls.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.persistence.collections.CollectionRepository;

import org.apache.commons.lang3.text.WordUtils;

import javax.inject.Inject;

import static com.aashreys.walls.ui.helpers.CollectionUiHelper.getIconForCollectionType;

/**
 * Created by aashreys on 04/02/17.
 */
public class CollectionView extends LinearLayout {

    @Inject CollectionRepository collectionRepository;

    private Collection collection;

    private ImageView iconImage;

    private TextView titleText;

    private ImageButton removeButton;

    private ImageView handleImage;

    public CollectionView(Context context) {
        super(context);
        _init(context, null, 0, 0);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs, 0, 0);
    }

    public CollectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CollectionView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(this);
        LayoutInflater.from(context).inflate(R.layout.layout_item_collection, this, true);
        iconImage = (ImageView) findViewById(R.id.image_icon);
        titleText = (TextView) findViewById(R.id.text_title);
        removeButton = (ImageButton) findViewById(R.id.button_remove);
        handleImage = (ImageView) findViewById(R.id.handle);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionRepository.remove(collection);
            }
        });
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
        titleText.setText(WordUtils.capitalizeFully(collection.getName().value()));
        iconImage.setImageResource(getIconForCollectionType(collection.getType()));
        removeButton.setImageResource(collection.isRemovable() ? R.drawable.ic_delete_black_24dp
                : R.drawable.ic_delete_black_inactive_24dp);
        removeButton.setEnabled(collection.isRemovable());
    }

    public void setDragHandleOnTouchListener(OnTouchListener listener) {
        handleImage.setOnTouchListener(listener);
    }
}
