package com.github.aliakseiKaraliou.onechatapp.logic.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public class ImageRounder {

    public Drawable makeRoundDrawable(final Context context, final Bitmap bitmap) {
        final Resources resources = context.getResources();
        final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
}
