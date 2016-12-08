package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LazyImageLoaderManager {

    private final LruCache<String, Drawable> cache = new LruCache<>(2 * 1024 * 1024);
    private final ImageLoader loader = new ImageLoader();
    private static Drawable roundedDefaultImage = null;

    public void load(final Context context, final ImageView imageView, final String url, final Bitmap defaultitmap) {
        Drawable image = cache.get(url);
        if (image == null) {
            final AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected void onPreExecute() {
                    final Drawable drawable = roundDrawable(context, defaultitmap);
                    imageView.setImageDrawable(drawable);
                    imageView.setTag(url);
                }

                @Override
                protected Bitmap doInBackground(Void... strings) {
                    return loader.loadFromUrl(url);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (imageView.getTag().equals(url) && bitmap!=null) {
                        final Drawable drawable = roundDrawable(context, bitmap);
                        cache.put(url, drawable);
                        imageView.setImageDrawable(drawable);
                    }
                }
            };
            try{
                asyncTask.execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            imageView.setImageDrawable(image);
        }
    }

    private Drawable roundDrawable(final Context context, final Bitmap bitmap) {
        final Resources resources = context.getResources();
        final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }

    private class ImageLoader {

        Bitmap loadFromUrl(String url) {
            try {
                final URL urlValue = new URL(url);
                final HttpURLConnection connection = (HttpURLConnection) urlValue.openConnection();
                connection.setDoInput(true);
                connection.connect();
                final InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
