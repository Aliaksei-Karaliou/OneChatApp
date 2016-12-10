package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.CHATS)
public final class ChatModel implements AbstractModel<ChatModel> {

    private static final String SOCIAL_NETWORK = "social_network";
    private static final String PEER_ID = "peerId";
    private static final String NAME = "name";
    private static final String PHOTO = "photo";

    @DbType(type = DbType.Type.INTEGER)
    @DbColumnName(name = PEER_ID)
    @DbPrimaryKey
    private long id;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = NAME)
    private String name;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO)
    private String photo;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = SOCIAL_NETWORK)
    @DbPrimaryKey
    private String socialNetwork;

    private ChatModel(long id, String name, String photo, SocialNetwork socialNetwork) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.socialNetwork = socialNetwork.toString();
    }

    private ChatModel(IChat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.photo = chat.getPhotoUrl();
        this.socialNetwork = chat.getSocialNetwork().toString();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PEER_ID, id);
        contentValues.put(NAME, name);
        contentValues.put(PHOTO, photo);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public ChatModel convertToModel(Cursor cursor) {
        final long peerId = cursor.getLong(0);
        final String name = cursor.getString(1);
        final String photoUrl = cursor.getString(2);
        final String social_network = cursor.getString(3);
        return new ChatModel(peerId, name, photoUrl, SocialNetwork.valueOf(social_network));
    }

    private ChatModel() {

    }

    public static ChatModel getInstance() {
        return new ChatModel();
    }

    public static List<ChatModel> convertTo(Collection<IChat> chatCollection) {
        List<ChatModel> modelList = new ArrayList<>();
        for (IChat chat : chatCollection) {
            modelList.add(new ChatModel(chat));
        }
        return modelList;
    }

    public static List<IChat> convertFrom(Collection<ChatModel> chatModelCollection) {
        List<IChat> chatList = new ArrayList<>();
        for (ChatModel chatModel : chatModelCollection) {
            IChat chat = new VkChat(chatModel.getId(), chatModel.getName(), chatModel.photo);
            chatList.add(chat);
        }
        return chatList;
    }
}
