package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;

public class ImageLoaderManager extends AsyncOperation<String, Bitmap> {

    private static LruCache<String, Bitmap> cache = new LruCache<>(1024 * 1024);

    @Override
    protected Bitmap doInBackground(final String url) {
        Bitmap result = cache.get(url);
        if (result != null) {
            return result;
        } else {
            result = new ImageLoader().loadFromUrl(url);
            cache.put(url, result);
            return result;
        }
    }
}
