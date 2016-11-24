package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//should be package-locale to have access only from ImageLoaderManager
class ImageLoader {

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
