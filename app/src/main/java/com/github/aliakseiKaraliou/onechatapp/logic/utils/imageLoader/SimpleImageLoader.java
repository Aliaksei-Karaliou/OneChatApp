package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleImageLoader extends AsyncOperation<String, Bitmap> {

    @Override
    protected Bitmap doInBackground(String url) {
        try {
            final URL urlValue = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) urlValue.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream inputStream = connection.getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
