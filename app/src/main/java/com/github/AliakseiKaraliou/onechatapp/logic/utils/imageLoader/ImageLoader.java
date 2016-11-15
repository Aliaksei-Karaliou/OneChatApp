package com.github.AliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

    private static OperationMemoryCache operationMemoryCache = new OperationMemoryCache();

    public Bitmap getBitmapFromUrl(String url) {
        final long idHash = url.hashCode();
        if (operationMemoryCache.contains(idHash))
            return operationMemoryCache.get(idHash);

        final AsyncOperation<String, Bitmap> asyncOperation = new AsyncOperation<String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String s) {
                try {
                    URL url = new URL(s);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    operationMemoryCache.put(idHash, bitmap);
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        try {
            return asyncOperation.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
