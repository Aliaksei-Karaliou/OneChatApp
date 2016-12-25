package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;
import java.util.Set;

public class VkReceiverDataParser implements IParser<Set<Long>, LongSparseArray<IReceiver>> {


    @Nullable
    @Override
    public LongSparseArray<IReceiver> parse(Set<Long> ids) {
        StringBuilder userIdSB = new StringBuilder();
        StringBuilder chatIdSB = new StringBuilder();
        StringBuilder groupIdSB = new StringBuilder();
        VkIdConverter converter = new VkIdConverter();
        for (Long id : ids) {
            if (VkReceiverStorage.get(id) == null) {
                if (id < 0)
                    new StringBuilder();
                if (id > VkIdConverter.getChatPeerOffset()) {
                    chatIdSB.append(converter.peerToChat(id)).append(",");
                } else if (id > 0 || id < VkIdConverter.getEmailPeerOffset()) {
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
                Pair<String, String> photosPair = new Pair<>(Constants.Json.FIELDS, Constants.Json.PHOTO_50_100);
                Pair<String, String> userIds = new Pair<>(Constants.Json.USER_IDS, userIdString);
                String userResponse = new VkRequester().doRequest(Constants.Method.USERS_GET, userIds, photosPair);
                userSparseArray = new VkUserDataParser().parse(userResponse);

            }

            if (chatIdSB.length() > 0) {
                final String chatIdString = chatIdSB.substring(0, chatIdSB.length() - 1);
                Pair<String, String> chatIds = new Pair<>(Constants.Json.CHAT_IDS, chatIdString);
                String chatResponse = new VkRequester().doRequest(Constants.Method.MESSAGES_GETCHAT, chatIds);
                chatSparseArray = new VkChatDataParser().parse(chatResponse);

            }

            if (groupIdSB.length() > 0) {
                final String groupIdString = groupIdSB.substring(0, groupIdSB.length() - 1);
                Pair<String, String> groupIds = new Pair<>(Constants.Json.GROUP_ID, groupIdString);
                String groupResponse = new VkRequester().doRequest(Constants.Method.GROUPS_GETBYID, groupIds);
                groupSparseArray = new VkGroupDataParser().parse(groupResponse);
            }

            LongSparseArray<IReceiver> result = new LongSparseArray<>();

            for (Long id : ids) {
                IReceiver receiver = VkReceiverStorage.get(id);
                if (receiver == null) {
                    if (id > VkIdConverter.getChatPeerOffset()) {  //chat
                        assert chatSparseArray != null;
                        receiver = chatSparseArray.get(id);
                    } else if (id > 0 || id < VkIdConverter.getEmailPeerOffset()) { //user or email
                        assert userSparseArray != null;
                        receiver = userSparseArray.get(id);
                    } else { //group
                        assert groupSparseArray != null;
                        receiver = groupSparseArray.get(id);
                    }
                }
                assert receiver != null;
                result.put(receiver.getId(), receiver);
            }
            return result;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
