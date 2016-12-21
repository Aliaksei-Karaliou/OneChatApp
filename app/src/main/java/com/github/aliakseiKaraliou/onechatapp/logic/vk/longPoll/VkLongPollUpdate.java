package com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.DialogListMessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.MessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkMessageByIdFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkMessageByIdStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VkLongPollUpdate {
    private Context context;
    private long ts;
    private List<List<String>> info;
    private byte MESSAGE_CODE = 4;

    public VkLongPollUpdate(Context context, long ts, @NonNull List<List<String>> info) {
        this.context = context;
        this.ts = ts;
        this.info = info;
    }

    public long getTs() {
        return ts;
    }

    public List<IEvent> getEvents() {
        List<IEvent> eventList = new ArrayList<>();
        StringBuilder newMessageIdStringBuilder = new StringBuilder();
        for (List<String> stringList : info) {
            byte code = Byte.parseByte(stringList.get(0));
            if (code == MESSAGE_CODE) {
                final long id = Long.parseLong(stringList.get(1));
                newMessageIdStringBuilder.append(id).append(',');
            }
        }
        final String messageIdString;
        if (newMessageIdStringBuilder.length() > 0) {
            try {
                messageIdString = newMessageIdStringBuilder.substring(0, newMessageIdStringBuilder.length() - 1);
                final Pair<String, String> messageIdPair = new Pair<>(Constants.Params.MESSAGE_IDS, messageIdString);
                final String request = new VkRequester().doRequest(Constants.Method.MESSAGES_GETBYID, messageIdPair);
                final Set<Long> parse = new VkMessageByIdStartParser().parse(request);
                if (parse != null && parse.size() > 0) {
                    final LongSparseArray<IReceiver> longSparseArrayStorage = new VkReceiverDataParser().parse(parse);
                    if (longSparseArrayStorage != null) {
                        for (int i = 0; i < longSparseArrayStorage.size(); i++) {
                                final IReceiver receiver = longSparseArrayStorage.valueAt(i);
                                VkReceiverStorage.put(receiver);
                        }
                    }
                }
                final List<IMessage> messageList = new VkMessageByIdFinalParser().parse(request);
                if (messageList != null) {
                    eventList.addAll(messageList);
                }
                final ORM messageORM = ((App) context.getApplicationContext()).getMessageORM();
                messageORM.insertAll(Constants.Db.ALL_MESSAGES, MessageModel.convertTo(messageList));
                messageORM.insertAll(Constants.Db.DIALOGS_LIST, DialogListMessageModel.convertTo(messageList));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        return eventList;
    }
}
