package com.github.aliakseiKaraliou.onechatapp.logic.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnectionChecker {

    public static boolean check(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
