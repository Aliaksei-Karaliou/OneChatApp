package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class SendManager {
    public boolean send(IReciever reciever, final java.lang.String message) {
        if (reciever.getSocialNetwork() == SocialNetwork.VK) {
            final AsyncOperation<Long, java.lang.String> asyncOperation = new AsyncOperation<Long, java.lang.String>() {
                @Override
                protected java.lang.String doInBackground(Long peerId) {
                    final Pair<java.lang.String, java.lang.String> peerIdPair = new Pair<java.lang.String, java.lang.String>(VkConstants.Params.PEER_ID, Long.toString(peerId));
                    final Pair<java.lang.String, java.lang.String> messagepair = new Pair<>(VkConstants.Params.MESSAGE, message);
                    try {
                        final java.lang.String s = new VkRequester().doRequest(VkConstants.Method.MESSAGES_SEND, peerIdPair, messagepair);
                        return s;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
            asyncOperation.startLoading(reciever.getId());
            final java.lang.String result = asyncOperation.getResult();
            return result != null && !result.contains("error");
        }
        return false;
    }
}
