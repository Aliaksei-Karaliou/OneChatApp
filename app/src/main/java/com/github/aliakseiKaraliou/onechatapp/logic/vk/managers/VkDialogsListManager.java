package com.github.aliakseiKaraliou.onechatapp.logic.vk.managers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class VkDialogsListManager {

    private AsyncOperation<Integer, List<IMessage>> asyncOperation;

    public void startLoading(final Context context, final int offset) {
        if (asyncOperation == null) {
            asyncOperation = new AsyncOperation<Integer, List<IMessage>>() {
                @Override
                protected List<IMessage> doInBackground(Integer offset) {
                    List<IMessage> messageList = null;
                    try {
                        final String jsonString;
                        if (offset > 0) {
                            Pair<String, String> offsetPair = new Pair<String, String>(VkConstants.Params.OFFSET, offset.toString());
                            jsonString = new VkRequester().doRequest(VkConstants.Method.MESSAGES_GETDIALOGS, offsetPair);
                        } else {
                            jsonString = new VkRequester().doRequest(VkConstants.Method.MESSAGES_GETDIALOGS);
                        }

                        Set<Long> idSet = new VkDialogsListStartParser().parse(jsonString);
                        final LongSparseArray<IReciever> parse = new VkReceiverDataParser().parse(idSet);
                        assert parse != null;
                        VkReceiverStorage.putAll(parse);
                        messageList = new VkDialogsListFinalParser().parse(context, jsonString, parse);
                        return messageList;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.startLoading(offset);
        }
    }

    @Nullable
    public List<IMessage> getResult() {
        if (asyncOperation != null) {
            final List<IMessage> result = asyncOperation.getResult();
            asyncOperation = null;
            return result;
        }
        return null;
    }
}
