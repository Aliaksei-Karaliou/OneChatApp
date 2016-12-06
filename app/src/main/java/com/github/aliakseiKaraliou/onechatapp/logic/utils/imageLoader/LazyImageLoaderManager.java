package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

public class LazyImageLoaderManager {

    private final LruCache<String, Bitmap> cache = new LruCache<>(2 * 1024 * 1024);
    private final ImageLoader loader = new ImageLoader();

    public void load(final Context context, final ImageView imageView, final String url, final Bitmap defaultImage) {
        Bitmap bitmap = cache.get(url);
        if (bitmap == null) {
            final AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {

                Bitmap result;

                @Override
                protected void onPreExecute() {
                    imageView.setImageBitmap(defaultImage);
                }

                @Override
                protected Bitmap doInBackground(Void... strings) {
                    result = loader.loadFromUrl(url);
                    return result;

                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                //    cache.put(url, result);
                    imageView.setImageBitmap(result);
                }
            };
            try{
                asyncTask.execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }
}
