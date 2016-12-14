package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public class ImageRounder {

    public static Drawable makeRoundDrawable(final Context context, final Bitmap bitmap) {
        final Resources resources = context.getResources();
        final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }

    public static Bitmap makeRoundBitmap(final Context context, final Bitmap bitmap) {
        final Resources resources = context.getResources();
        final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        roundedBitmapDrawable.setCircular(true);
        final Drawable drawable = makeRoundDrawable(context, bitmap);
        final BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        final Bitmap bitmap1 = bitmapDrawable.getBitmap();
        new StringBuilder();
        return bitmap1;
    }
}
