package com.example.korol.onechatapp.logic.vk;

public class VkInfo {
    public int getAppId() {
        return 5504374;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        VkInfo.userId = userId;
    }

    private static int userId = -1;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        VkInfo.accessToken = accessToken;
    }

    private static String accessToken = null;

    public VkInfo(String url) {
        String[] values = url.split("#")[1].split("&");
        accessToken = values[0].split("=")[1];
        userId = Integer.parseInt(values[2].split("=")[1]);
    }

    public VkInfo() {
    }
}
