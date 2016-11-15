package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.support.annotation.Nullable;

import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public final class VkLongPoll {

    private String key;
    private String server;
    private long ts;
    private Thread thread;

    private VkLongPoll(String key, String server, long ts) {
        this.key = key;
        this.server = server;
        this.ts = ts;
    }

    @Nullable
    public static VkLongPoll initialize() {
        try {
            JSONObject jsonObject = new JSONObject(new VkRequester("messages.getLongPollServer").execute(null)).getJSONObject("response");
            String key = jsonObject.getString("key");
            String server = jsonObject.getString("server");
            long ts = jsonObject.getLong("ts");
            return new VkLongPoll(key, server, ts);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        JSONObject json = new JSONObject(new VkLongPollRequester().execute(null));
                        ts = json.getLong("ts");
                        JSONArray array = json.getJSONArray("updates");
                        final StringBuilder builder = new StringBuilder();
                    }
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class VkLongPollRequester extends AsyncOperation<Void, String> {

        @Override
        protected String doInBackground(Void aVoid) {
            String stringUrl = String.format(Locale.getDefault(), "https://%s?act=a_check&key=%s&ts=%d&wait=25&mode=2&version=1", server, key, ts);
            new StringBuilder();
            try {
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
