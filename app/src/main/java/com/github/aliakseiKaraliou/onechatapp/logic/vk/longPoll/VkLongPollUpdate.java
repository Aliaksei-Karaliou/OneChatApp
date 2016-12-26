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
import com.github.aliakseiKaraliou.onechatapp.logic.utils.exceptions.UnknownMessageException;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlagConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkAddNewMessageEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkReadAllMessagesEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkAddMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkChangeMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkDeleteMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkMessageByIdFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkMessageByIdStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkMessageStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VkLongPollUpdate {
    private final Context context;
    private final long ts;
    private final List<List<String>> info;

    private static final byte CHANGE_MESSAGE_FLAGS_CODE = 1;
    private static final byte ADD_MESSAGE_FLAGS_CODE = 2;
    private static final byte DELETE_MESSAGE_FLAGS_CODE = 3;
    private static final byte NEW_MESSAGE_CODE = 4;
    private static final byte READ_ALL_MESSAGES_CODE = 6;


    public VkLongPollUpdate(final Context context, final long ts, @NonNull final List<List<String>> info) {
        this.context = context;
        this.ts = ts;
        this.info = info;
    }

    public long getTs() {
        return ts;
    }

    public List<IEvent> getEvents() throws UnknownMessageException {
        final List<IEvent> eventList = new ArrayList<>();
        final StringBuilder newMessageIdStringBuilder = new StringBuilder();
        for (final List<String> stringList : info) {
            final byte code = Byte.parseByte(stringList.get(0));

            if (code == NEW_MESSAGE_CODE) {
                final long id = Long.parseLong(stringList.get(1));
                newMessageIdStringBuilder.append(id).append(',');
            } else if (code == CHANGE_MESSAGE_FLAGS_CODE || code == DELETE_MESSAGE_FLAGS_CODE || code == ADD_MESSAGE_FLAGS_CODE) {

                final long id = Long.parseLong(stringList.get(1));
                final IMessage message = VkMessageStorage.get(id);

                final int flagId = Integer.parseInt(stringList.get(2));
                final Set<VkMessageFlag> messageFlags = new VkMessageFlagConverter().Convert(flagId);
                for (final VkMessageFlag messageFlag : messageFlags) {

                    final IEvent event;

                    if (code == ADD_MESSAGE_FLAGS_CODE) {
                        event = new VkAddMessageFlagEvent(message, messageFlag);
                    } else if (code == DELETE_MESSAGE_FLAGS_CODE) {
                        event = new VkDeleteMessageFlagEvent(message, messageFlag);
                    } else {
                        event = new VkChangeMessageFlagEvent(message, messageFlag);
                    }

                    eventList.add(event);
                }
            } else if (code == READ_ALL_MESSAGES_CODE) {
                final long id = Long.parseLong(stringList.get(2));
                final IMessage message = VkMessageStorage.get(id);
                if (message == null) {
                    throw new UnknownMessageException();
                }
                final IEvent event = new VkReadAllMessagesEvent(message);
                eventList.add(event);
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
                VkMessageStorage.putAll(messageList);
                if (messageList != null) {
                    for (final IMessage message : messageList) {
                        eventList.add(new VkAddNewMessageEvent(message));
                    }
                }
                final ORM messageORM = ((App) context.getApplicationContext()).getMessageORM();
                messageORM.insertAll(Constants.Db.ALL_MESSAGES, MessageModel.convertTo(messageList));
                messageORM.insertAll(Constants.Db.DIALOGS_LIST, DialogListMessageModel.convertTo(messageList));

            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return eventList;
    }
}
