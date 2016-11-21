package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.support.v4.util.LongSparseArray;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.parsers.VkMessageListParser;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class VkDialogsListManager {

    private AsyncOperation<Void, List<IMessage>> asyncOperation;

    public void startLoading() {
        asyncOperation = new AsyncOperation<Void, List<IMessage>>() {
            @Override
            protected List<IMessage> doInBackground(Void aVoid) {
                List<IMessage> messageList = null;
                try {
                    final String jsonString = new VkRequester().doRequest("messages.getDialogs");
                    Set<Long> idSet = new VkMessageListParser().parse(jsonString);
                    final LongSparseArray<IReciever> parse = new VkReceiverDataParser().parse(idSet);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return messageList;
            }
        }.startLoading(null);
    }

    public List<IMessage> getResult() {
        return asyncOperation.getResult();
    }

}
