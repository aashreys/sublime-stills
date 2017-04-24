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

package com.aashreys.walls.application.views.libs;


import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by aashreys on 19/04/17.
 */

public class AspectRatioImageView extends ForegroundImageView {

    private float widthToHeightRatio;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWidthToHeightRatio(float widthToHeightRatio) {
        this.widthToHeightRatio = widthToHeightRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthToHeightRatio > 0) {
            int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
            int calculatedHeight = (int) (originalWidth / widthToHeightRatio);
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY)
            );
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
