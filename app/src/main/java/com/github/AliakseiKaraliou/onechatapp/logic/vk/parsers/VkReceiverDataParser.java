package com.github.AliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class VkReceiverDataParser implements IParser<Set<Long>, LongSparseArray<IReciever>> {

    @Nullable
    @Override
    public LongSparseArray<IReciever> parse(Set<Long> ids) {
        StringBuilder userIdSB = new StringBuilder();
        StringBuilder chatIdSB = new StringBuilder();
        StringBuilder groupIdSB = new StringBuilder();
        VkIdConverter converter = new VkIdConverter();
        for (Long id : ids) {
            if (id > VkIdConverter.getChatPeerOffset()) {
                chatIdSB.append(converter.peerToChat(id)).append(",");
            } else if (id > 0) {
                userIdSB.append(id).append(",");
            } else {
                groupIdSB.append(converter.peerToGroup(id)).append(",");
            }
        }

        //removing last ,
        final String userIdString = userIdSB.substring(0, userIdSB.length() - 1);
        final String chatIdString = chatIdSB.substring(0, chatIdSB.length() - 1);
        final String groupIdString = groupIdSB.substring(0, groupIdSB.length() - 1);

        try {
            Pair<String, String> fields = new Pair<>(VkConstants.Json.FIELDS, VkConstants.Json.PHOTO_50);
            Pair<String, String> userIds = new Pair<>(VkConstants.Json.USER_IDS, userIdString);
            String userResponse = new VkRequester().doRequest(VkConstants.Method.USERS_GET, userIds, fields);
            final List<IUser> users = new VkUserDataParser().parse(userResponse);

            Pair<String, String> chatIds = new Pair<>(VkConstants.Json.CHAT_IDS, chatIdString);
            String chatResponse = new VkRequester().doRequest(VkConstants.Method.MESSAGES_GETCHAT, chatIds);
            final List<IChat> chats = new VkChatDataParser().parse(chatResponse);

            Pair<String, String> groupIds = new Pair<>(VkConstants.Json.GROUP_ID, groupIdString);
            String groupResponse = new VkRequester().doRequest(VkConstants.Method.GROUPS_GETBYID, groupIds);

            new StringBuilder();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
