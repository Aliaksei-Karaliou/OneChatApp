package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ImageLoader<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {

    protected final Bitmap loadImageFromUrl(final String url){
        try {
            final URL urlValue = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) urlValue.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
