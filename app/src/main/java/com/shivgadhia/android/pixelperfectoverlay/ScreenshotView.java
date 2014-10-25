package com.shivgadhia.android.pixelperfectoverlay;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

class ScreenshotView extends ImageView {

    private static final int IMAGE_ALPHA = 150;

    public ScreenshotView(Context context, Uri uri) {
        super(context);

        setImageURI(uri);
        setScaleType(ScaleType.FIT_XY);
        setImageAlpha(IMAGE_ALPHA);
        setColorFilter(getResources().getColor(android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
}