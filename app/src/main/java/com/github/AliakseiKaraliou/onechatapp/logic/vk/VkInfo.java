package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class VkInfo {

    private static final String ACCESS_TOKEN = "Access Token";
    private static final String USER_ID = "User Id";



    public static double getVkApiVersion() {
        return 5.60;
    }

    public static int getAppId() {
        return 5504374;
    }

    public static long getUserId() {
        return userId;
    }

    //Available only in this package
    static void setUserId(long userId) {
        VkInfo.userId = userId;
    }

    private static long userId = -1;

    public static String getAccessToken() {
        return accessToken;
    }

    //Available only in this package
    static void setAccessToken(String accessToken) {
        VkInfo.accessToken = accessToken;
    }

    private static String accessToken = null;

    public static void userSetAuth(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN, VkInfo.getAccessToken());
        editor.putLong(USER_ID, VkInfo.getUserId());
        editor.apply();
    }

    public static void userGetAuth(SharedPreferences sharedPreferences) {
        final long id = sharedPreferences.getLong(USER_ID, -1);
        VkInfo.setUserId(id);
        VkInfo.setAccessToken(sharedPreferences.getString(ACCESS_TOKEN, null));
    }

    public static boolean isUserAuthorized() {
        return userId > 0 && accessToken != null;
    }
}