package com.example.korol.onechatapp.logic.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.korol.onechatapp.logic.assyncOperation.AssyncOperation;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

    public Bitmap getBitmapFromUrl(String url) {
        final AssyncOperation<String, Bitmap> assyncOperation = new AssyncOperation<String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String s) {
                try {
                    URL url = new URL(s);
                    HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        try {
            return assyncOperation.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
