package com.example.korol.onechatapp.logic.vk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class VkInfo {

    private static final String ACCESS_TOKEN = "Access Token";
    private static final String USER_ID = "User Id";

    public static void setUrl(String url) {
        String[] values = url.split("#")[1].split("&");
        accessToken = values[0].split("=")[1];
        userId = Integer.parseInt(values[2].split("=")[1]);
        if (userId > 0 && accessToken != null)
            authorized = true;
    }

    public static double getVkApiVersion() {
        return 5.59;
    }

    public static int getAppId() {
        return 5504374;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        VkInfo.userId = userId;
    }

    private static int userId = -1;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        VkInfo.accessToken = accessToken;
    }

    private static String accessToken = null;

    public static void userSetAuth(Activity activity) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(ACCESS_TOKEN, VkInfo.getAccessToken());
        editor.putInt(USER_ID, VkInfo.getUserId());
        editor.apply();
    }

    public static void userGetAuth(Activity activity) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        VkInfo.setUserId(preferences.getInt(USER_ID, -1));
        VkInfo.setAccessToken(preferences.getString(ACCESS_TOKEN, null));
        if (VkInfo.getUserId() > 0) {
            authorized = true;
        }
    }

    public static boolean isAuthorized() {
        return authorized;
    }

    private static void setAuthorized(boolean authorized) {
        VkInfo.authorized = authorized;
    }

    private static boolean authorized;
}
