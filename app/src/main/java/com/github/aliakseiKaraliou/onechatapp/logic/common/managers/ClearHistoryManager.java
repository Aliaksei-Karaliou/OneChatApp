package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.os.AsyncTask;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class ClearHistoryManager {
    public void clear(final IReceiver receiver) {
        final long peerId = receiver.getId();
        new AsyncTask<IReceiver, Void, String>() {
            @Override
            protected String doInBackground(final IReceiver... params) {
                try {
                    final Pair<java.lang.String, java.lang.String> peerIdPair = new Pair<>(Constants.Params.PEER_ID, Long.toString(peerId));
                    return new VkRequester().doRequest(Constants.Method.MESSAGES_DELETEDIALOG, peerIdPair);
                } catch (final IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final String result) {
                super.onPostExecute(result);
                if (result == null || !result.equals("{\"response\":1}")){
                    throw new RuntimeException();
                }
            }
        }.execute(receiver);
    }
}
