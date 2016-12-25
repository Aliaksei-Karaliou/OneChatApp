package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class SendManager {
    public boolean send(final IReceiver reciever, final String message) {
        if (reciever.getSocialNetwork() == SocialNetwork.VK) {
            final AsyncOperation<Long, String> asyncOperation = new AsyncOperation<Long, String>() {
                @Override
                protected String doInBackground(Long peerId) {
                    final Pair<String, String> peerIdPair = new Pair<>(Constants.Params.PEER_ID, Long.toString(peerId));
                    final Pair<String, String> messagepair = new Pair<>(Constants.Params.MESSAGE, message);
                    try {
                        return new VkRequester().doRequest(Constants.Method.MESSAGES_SEND, peerIdPair, messagepair);
                    } catch (final IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
            asyncOperation.startLoading(reciever.getId());
            final String result = asyncOperation.getResult();
            return result != null && !result.contains("error");
        }
        return false;
    }
}
