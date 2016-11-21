package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class VkRequester {

    @SafeVarargs
    public final String doRequest(String methodName, Pair<String, String>... params) throws IOException {

        StringBuilder paramsBuilder = new StringBuilder();
        String stringParams = "";
        if (params.length > 0) {
            for (Pair<String, String> param : params) {
                paramsBuilder.append(param.first).append("=").append(param.second.replace(" ", "%20")).append("&");
            }
            stringParams = paramsBuilder.toString().substring(0, paramsBuilder.length() - 1);
        }

        String stringUrl = String.format(Locale.US, "https://api.vk.com/method/%s?%s&access_token=%s&v=%.2f", methodName, stringParams, VkInfo.getAccessToken(), VkInfo.getVkApiVersion());

        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            bufferedInputStream.close();
            reader.close();
            connection.disconnect();
            return builder.toString();
        } else
            throw new IOException();
    }

}
