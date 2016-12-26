package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.widget.ImageView;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.ImageRounder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AvatarImageLoaderManager {

    private final LruCache<String, Drawable> cache = new LruCache<>(2 * 1024 * 1024);
    private final Executor executor = Executors.newFixedThreadPool(20);
    private final ImageRounder rounder = new ImageRounder();

    public void load(final Context context, final ImageView imageView, final String url, final Bitmap defaultBitmap) {
        final Drawable image = cache.get(url);

        if (image == null) {
            final ImageLoader<Void, Void, Bitmap> imageLoader = new ImageLoader<Void, Void, Bitmap>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    final Drawable drawable = rounder.makeRoundDrawable(context, defaultBitmap);
                    imageView.setImageDrawable(drawable);
                    imageView.setTag(url);
                    new StringBuilder();
                }

                @Override
                protected Bitmap doInBackground(final Void... params) {
                    return loadImageFromUrl(url);
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    try {
                        final Object tag = imageView.getTag();
                        if (bitmap != null && tag.equals(url)) {
                            final Drawable drawable = rounder.makeRoundDrawable(context, bitmap);
                            imageView.setImageDrawable(drawable);
                        }
                    }
                    catch (final Exception e){
                        e.printStackTrace();
                        throw new IllegalArgumentException();
                    }
                }
            };
            imageLoader.executeOnExecutor(executor);
        } else {
            imageView.setImageDrawable(image);
        }
    }

}
