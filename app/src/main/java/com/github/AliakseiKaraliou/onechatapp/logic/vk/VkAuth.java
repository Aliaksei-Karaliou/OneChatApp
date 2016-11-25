package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VkAuth {


    private int appId;

    public int getAppId() {
        return appId;
    }

    private List<String> scope;

    public List<String> getScope() {
        return scope;
    }

    public void setScope(@NonNull String... scope) {
        if (this.scope == null)
            this.scope = Arrays.asList(scope);
        else
            this.scope.addAll(Arrays.asList(scope));
    }

    @NonNull
    private String scopeToString(List<String> scope) {
        if (scope.size() == 0)
            return "";
        else if (scope.size() == 1)
            return scope.get(0);
        else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < scope.size() - 1; i++)
                builder.append(scope.get(i)).append('&');
            return builder.append(scope.get(scope.size() - 1)).toString();
        }
    }

    public VkAuth(int appId) {
        this.appId = appId;
    }

    @SuppressLint("DefaultLocale")
    public String getUrl() {
        //String string = String.format("https://oauth.vk.com/authorize?client_id=%d&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=%s&response_type=token&v=5.57&revoke=1", appId, scopeToString(scope));
        return String.format(Locale.US, "https://oauth.vk.com/authorize?client_id=%d&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=offline,%s&response_type=token&v=%f&revoke=1", appId, scopeToString(scope), VkInfo.getVkApiVersion());
    }

    public static void parseUrl(String url) {
        url = url.replace('#', '?');
        Uri uri = Uri.parse(url);
        VkInfo.setUserId(Long.parseLong(uri.getQueryParameter("user_id")));
        VkInfo.setAccessToken(uri.getQueryParameter("access_token"));
    }
}
