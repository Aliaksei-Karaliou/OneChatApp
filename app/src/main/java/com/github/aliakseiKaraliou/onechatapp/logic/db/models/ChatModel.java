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
    private static final String PHOTO50 = "photo50";
    private static final String PHOTO100 = "photo100";

    @DbType(type = DbType.Type.INTEGER)
    @DbColumnName(name = PEER_ID)
    @DbPrimaryKey
    private long id;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = NAME)
    private String name;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO50)
    private String photo50Url;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO100)
    private String photo100Url;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = SOCIAL_NETWORK)
    @DbPrimaryKey
    private String socialNetwork;

    private ChatModel(long id, String name, String photo50Url, String photo100Url, SocialNetwork socialNetwork) {
        this.id = id;
        this.name = name;
        this.photo50Url = photo50Url;
        this.photo100Url = photo100Url;
        this.socialNetwork = socialNetwork.toString();
    }

    private ChatModel(IChat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.photo50Url = chat.getPhoto50Url();
        this.photo100Url = chat.getPhoto100Url();
        this.socialNetwork = chat.getSocialNetwork().toString();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto50Url() {
        return photo50Url;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PEER_ID, id);
        contentValues.put(NAME, name);
        contentValues.put(PHOTO50, photo50Url);
        contentValues.put(PHOTO100, photo100Url);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public ChatModel convertToModel(Cursor cursor) {
        final long peerId = cursor.getLong(0);
        final String name = cursor.getString(1);
        final String photo100 = cursor.getString(2);
        final String photo50 = cursor.getString(3);
        final String social_network = cursor.getString(4);
        return new ChatModel(peerId, name, photo50, photo100, SocialNetwork.valueOf(social_network));
    }

    private ChatModel() {

    }

    public static ChatModel getInstance() {
        return new ChatModel();
    }

    public static List<ChatModel> convertTo(Collection<IChat> chatCollection) {
        List<ChatModel> modelList = new ArrayList<>();
        for (IChat chat : chatCollection) {
            modelList.add(convertTo(chat));
        }
        return modelList;
    }

    public static ChatModel convertTo(IChat chat){
        return new ChatModel(chat);
    }

    public static List<IChat> convertFrom(Collection<ChatModel> chatModelCollection) {
        List<IChat> chatList = new ArrayList<>();
        for (ChatModel chatModel : chatModelCollection) {
            chatList.add(convertFrom(chatModel));
        }
        return chatList;
    }

    public static IChat convertFrom(ChatModel chatModel){
        return new VkChat(chatModel.getId(), chatModel.getName(), chatModel.photo50Url, chatModel.photo100Url);
    }
}
