package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.support.annotation.Nullable;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class VkRequester {

    public VkRequester() {
    }

    @SafeVarargs
    public synchronized final String doRequest(String methodName, Pair<String, String>... params) throws IOException {

        try {
            final StringBuilder paramsBuilder = new StringBuilder();
            String stringParams = "";
            if (params.length > 0) {
                for (Pair<String, String> param : params) {
                    paramsBuilder.append(param.first).append("=").append(param.second.replace(" ", "%20")).append("&");
                }
                stringParams = paramsBuilder.toString().substring(0, paramsBuilder.length() - 1);
            }

            String stringUrl = String.format(Locale.US, Constants.Other.VK_REQUEST_TEMPLATE, methodName, stringParams, VkInfo.getAccessToken(), VkInfo.getVkApiVersion());

            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Thread.sleep(400);
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final String result = readConnection(connection);
                connection.disconnect();
                return result;
            } else {
                throw new IOException();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public synchronized final String doLongPollRequest(VkLongPollServer server) {
        try {
            final String urlString = String.format(Locale.US, Constants.Other.VK_LONG_POLL_REQUEST, server.getServer(), server.getKey(), server.getTs());
            try {
                final URL url = new URL(urlString);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final String result = readConnection(connection);
                    connection.disconnect();
                    return result;
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readConnection(HttpURLConnection connection) {
        try {
            final BufferedInputStream stream = new BufferedInputStream(connection.getInputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
