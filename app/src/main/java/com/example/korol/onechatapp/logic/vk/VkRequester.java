package com.example.korol.onechatapp.logic.vk;

import android.util.Pair;

import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class VkRequester extends AsyncOperation<Void, String> {

    private String methodName;

    private Pair<String, String>[] params;

    private String stringParams = "";

    public VkRequester(String methodName, Pair<String, String>... params) {
        this.methodName = methodName;
        this.params = params;
        StringBuilder paramsBuilder = new StringBuilder();
        if (params.length > 0) {
            for (Pair<String, String> param : params)
                paramsBuilder.append(param.first).append("=").append(param.second).append("&");
            stringParams = paramsBuilder.toString().substring(0, paramsBuilder.length() - 1);
            final StringBuilder stringBuilder = new StringBuilder();
        }
    }

    private String getJSON() throws IOException {
        String stringUrl = String.format(Locale.getDefault(), "https://api.vk.com/method/%s?%s&access_token=%s&v=%.2f", methodName, stringParams, VkInfo.getAccessToken(), VkInfo.getVkApiVersion());

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

    @Override
    protected String doInBackground(Void aVoid) {
        try {
            return getJSON();
        } catch (IOException e) {
            return "Error request";
        }
    }
}
