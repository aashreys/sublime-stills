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

/**
 * Created by aashreys on 04/02/17.
 */
public class CollectionView extends LinearLayout implements CollectionViewModel.EventCallback {

    private CollectionViewModel viewModel;

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
        createViewModel();
        viewModel.setEventCallback(this);
        LayoutInflater.from(context).inflate(R.layout.layout_item_collection, this, true);
        iconImage = (ImageView) findViewById(R.id.image_icon);
        titleText = (TextView) findViewById(R.id.text_title);
        removeButton = (ImageButton) findViewById(R.id.button_remove);
        handleImage = (ImageView) findViewById(R.id.handle);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onRemoveButtonClicked();
            }
        });
    }

    private void createViewModel() {
        viewModel = new CollectionViewModel();
        ((WallsApplication) getContext().getApplicationContext()).getApplicationComponent()
                .getUiComponent()
                .inject(viewModel);
    }

    public void setCollection(Collection collection) {
        viewModel.setCollection(collection);
    }

    public void setDragHandleOnTouchListener(OnTouchListener listener) {
        handleImage.setOnTouchListener(listener);
    }

    @Override
    public void onCollectionSet() {
        titleText.setText(viewModel.getCollectionName());
        iconImage.setImageResource(viewModel.getCollectionIcon());
        removeButton.setImageResource(viewModel.getDeleteButtonIcon());
    }
}
