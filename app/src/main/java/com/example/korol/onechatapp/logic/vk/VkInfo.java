package com.example.korol.onechatapp.logic.vk;

public class VkInfo {

    public static void setUrl(String url) {
        String[] values = url.split("#")[1].split("&");
        accessToken = values[0].split("=")[1];
        userId = Integer.parseInt(values[2].split("=")[1]);
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
}
