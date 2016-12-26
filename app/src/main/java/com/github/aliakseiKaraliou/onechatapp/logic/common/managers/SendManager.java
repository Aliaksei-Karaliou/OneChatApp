package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.os.AsyncTask;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

public class SendManager {

    private static final String ERROR = "ERROR";

    public void send(final IReceiver reciever, final String message) {

        final long peerId = reciever.getId();
        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(final Void... params) {
                final Pair<String, String> peerIdPair = new Pair<>(Constants.Params.PEER_ID, Long.toString(peerId));
                final Pair<String, String> messagepair = new Pair<>(Constants.Params.MESSAGE, message);
                try {
                    return new VkRequester().doRequest(Constants.Method.MESSAGES_SEND, peerIdPair, messagepair);
                } catch (final Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final String result) {
                if (result == null || result.contains(ERROR)) {
                    throw new RuntimeException();
                }
            }
        }.execute();
    }
}
