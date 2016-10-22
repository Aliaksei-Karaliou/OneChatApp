package com.example.korol.onechatapp.logic.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

public class ImageFromUrl {

    public static Bitmap getImageFromUrl(String url){
        try {
            InputStream in = new URL(url).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            // log error
        }
        return null;
    }
}
