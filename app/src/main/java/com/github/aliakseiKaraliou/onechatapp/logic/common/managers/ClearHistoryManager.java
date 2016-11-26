package com.github.aliakseiKaraliou.onechatapp.logic.common.managers;

import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class ClearHistoryManager {
    public boolean clear(IReciever reciever) {
        if (reciever.getSocialNetwork() == SocialNetwork.VK) {
            final AsyncOperation<Long, String> asyncOperation = new AsyncOperation<Long, String>() {
                @Override
                protected String doInBackground(Long peerId) {
                    try {
                        final Pair<String, String> peerIdPair = new Pair<>(VkConstants.Params.PEER_ID, Long.toString(peerId));
                        return new VkRequester().doRequest(VkConstants.Method.MESSAGES_DELETEDIALOG, peerIdPair);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
            asyncOperation.startLoading(reciever.getId());
            final String result = asyncOperation.getResult();
            return result != null && result.equals("{\"response\":1}");
        }
        return false;
    }
}
