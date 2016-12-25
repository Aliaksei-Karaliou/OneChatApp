package com.github.aliakseiKaraliou.onechatapp.logic.vk.managers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.ChatModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.DialogListMessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.GroupModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.MessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.UserModel;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public final class VkDialogsListManager {

    private AsyncOperation<Integer, List<IMessage>> asyncOperation;

    public void startLoading(final Context context, final int offset) {
        if (asyncOperation == null) {
            asyncOperation = new AsyncOperation<Integer, List<IMessage>>() {
                @Override
                protected List<IMessage> doInBackground(Integer offset) {
                    List<IMessage> messageList;
                    try {
                        final String jsonString;
                        if (offset > 0) {
                            final Pair<String, String> offsetPair = new Pair<>(Constants.Params.OFFSET, offset.toString());
                            jsonString = new VkRequester().doRequest(Constants.Method.MESSAGES_GETDIALOGS, offsetPair);
                        } else {
                            jsonString = new VkRequester().doRequest(Constants.Method.MESSAGES_GETDIALOGS);
                        }

                        final Set<Long> idSet = new VkDialogsListStartParser().parse(jsonString);
                        final LongSparseArray<IReceiver> parse = new VkReceiverDataParser().parse(idSet);

                        final List<IUser> userList = new ArrayList<>();
                        final List<IChat> chatList = new ArrayList<>();
                        final List<IGroup> groupList = new ArrayList<>();
                        if (parse != null && parse.size()>0) {
                            for (int i = 0; i < parse.size(); i++) {
                                final IReceiver reciever = parse.valueAt(i);
                                if (reciever instanceof IUser)
                                    userList.add(((IUser) reciever));
                                else if (reciever instanceof IChat)
                                    chatList.add(((IChat) reciever));
                                else if (reciever instanceof IGroup)
                                    groupList.add(((IGroup) reciever));
                            }
                        }
                        ORM orm = ((App) context.getApplicationContext()).getRecieverORM();
                        orm.insertAll(Constants.Db.USERS, UserModel.convertTo(userList));
                        orm.insertAll(Constants.Db.CHATS, ChatModel.convertTo(chatList));
                        orm.insertAll(Constants.Db.GROUPS, GroupModel.convertTo(groupList));

                        VkReceiverStorage.putAll(parse);

                        messageList = new VkDialogsListFinalParser().parse(context, jsonString);

                        final ORM messageORM = ((App) context.getApplicationContext()).getMessageORM();
                        messageORM.insertAll(Constants.Db.DIALOGS_LIST, DialogListMessageModel.convertTo(messageList));
                        messageORM.insertAll(Constants.Db.ALL_MESSAGES, MessageModel.convertTo(messageList));

                        return messageList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
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
