package com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

public class InternalStorager {

    private Context context;

    public InternalStorager(Context context) {
        this.context = context;
    }

    public boolean saveBitmap(Bitmap image, String filename) {
        FileOutputStream outputStream;
        try {
            String path = context.getCacheDir().toString() + '/' + filename;
            outputStream = new FileOutputStream(new File((path)), true);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            final boolean exists = new File(path).exists();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
