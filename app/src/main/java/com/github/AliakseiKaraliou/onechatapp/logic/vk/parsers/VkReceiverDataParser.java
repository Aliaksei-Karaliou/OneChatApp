package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;
import java.util.Set;

public class VkReceiverDataParser implements IParser<Set<Long>, LongSparseArray<IReciever>> {


    @Nullable
    @Override
    public LongSparseArray<IReciever> parse(Set<Long> ids) {
        StringBuilder userIdSB = new StringBuilder();
        StringBuilder chatIdSB = new StringBuilder();
        StringBuilder groupIdSB = new StringBuilder();
        VkIdConverter converter = new VkIdConverter();
        int i = 0;
        for (Long id : ids) {
            if (VkReceiverStorage.get(id) == null) {
                if (id > VkIdConverter.getChatPeerOffset()) {
                    chatIdSB.append(converter.peerToChat(id)).append(",");
                } else if (id > 0) {
                    userIdSB.append(id).append(",");
                } else {
                    groupIdSB.append(converter.peerToGroup(id)).append(",");
                }
            }
        }

        LongSparseArray<IUser> userSparseArray = null;
        LongSparseArray<IChat> chatSparseArray = null;
        LongSparseArray<IGroup> groupSparseArray = null;

        try {
            if (userIdSB.length() > 0) {
                final String userIdString = userIdSB.substring(0, userIdSB.length() - 1);
                Pair<String, String> fields = new Pair<>(VkConstants.Json.FIELDS, VkConstants.Json.PHOTO_50);
                Pair<String, String> userIds = new Pair<>(VkConstants.Json.USER_IDS, userIdString);
                String userResponse = new VkRequester().doRequest(VkConstants.Method.USERS_GET, userIds, fields);
                userSparseArray = new VkUserDataParser().parse(userResponse);
            }

            if (chatIdSB.length() > 0) {
                final String chatIdString = chatIdSB.substring(0, chatIdSB.length() - 1);
                Pair<String, String> chatIds = new Pair<>(VkConstants.Json.CHAT_IDS, chatIdString);
                String chatResponse = new VkRequester().doRequest(VkConstants.Method.MESSAGES_GETCHAT, chatIds);
                chatSparseArray = new VkChatDataParser().parse(chatResponse);
            }

            if (groupIdSB.length() > 0) {
                final String groupIdString = groupIdSB.substring(0, groupIdSB.length() - 1);
                Pair<String, String> groupIds = new Pair<>(VkConstants.Json.GROUP_ID, groupIdString);
                String groupResponse = new VkRequester().doRequest(VkConstants.Method.GROUPS_GETBYID, groupIds);
                groupSparseArray = new VkGroupDataParser().parse(groupResponse);
            }

            LongSparseArray<IReciever> result = new LongSparseArray<>();

            for (Long id : ids) {
                IReciever reciever = VkReceiverStorage.get(id);
                if (reciever == null) {
                    if (id > VkIdConverter.getChatPeerOffset()) {  //chat
                        assert chatSparseArray != null;
                        reciever = chatSparseArray.get(id);
                    } else if (id > 0) { //user
                        assert userSparseArray != null;
                        reciever = userSparseArray.get(id);
                    } else { //group
                        assert userSparseArray != null;
                        reciever = groupSparseArray.get(id);
                    }
                }
                assert reciever != null;
                result.put(reciever.getId(), reciever);
            }
            return result;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
