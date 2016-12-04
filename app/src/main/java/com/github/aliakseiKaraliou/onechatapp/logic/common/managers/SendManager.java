package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class SendManager {
    public boolean send(IReciever reciever, final String message) {
        if (reciever.getSocialNetwork() == SocialNetwork.VK) {
            final AsyncOperation<Long, String> asyncOperation = new AsyncOperation<Long, String>() {
                @Override
                protected String doInBackground(Long peerId) {
                    final Pair<String, String> peerIdPair = new Pair<>(VkConstants.Params.PEER_ID, Long.toString(peerId));
                    final Pair<String, String> messagepair = new Pair<>(VkConstants.Params.MESSAGE, message);
                    try {
                        final String s = new VkRequester().doRequest(VkConstants.Method.MESSAGES_SEND, peerIdPair, messagepair);
                        return s;
                    } catch (IOException e) {
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
