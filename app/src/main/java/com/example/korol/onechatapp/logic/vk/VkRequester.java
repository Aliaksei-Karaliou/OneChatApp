package com.example.korol.onechatapp.logic.vk;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class VkRequester extends AsyncTask<Void, Void, String> {

    private String methodName;

    private Pair<String, String>[] params;

    public VkRequester(String methodName, Pair<String, String>... params) {
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return getJSON();
        } catch (IOException e) {
            return "Error request";
        }
    }

    private String getJSON() throws IOException {
        String stringUrl = String.format("https://api.vk.com/method/%s?access_token=%s&v=", methodName, VkInfo.getAccessToken());

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
