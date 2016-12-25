package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.ALL_MESSAGES)
public final class MessageModel implements AbstractModel<MessageModel> {

    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String TIMESTAMP = "timestamp";
    private static final String PEER_ID = "peerId";
    private static final String USER_ID = "userId";
    private static final String IS_READ = "isRead";
    private static final String IS_OUT = "isOut";
    private static final String SOCIAL_NETWORK = "socialNetwork";
    private static final String FLAGS = "flags";

    @DbPrimaryKey
    @DbColumnName(name = ID)
    @DbType(type = DbType.Type.INTEGER)
    private long id;

    @DbColumnName(name = TEXT)
    @DbType(type = DbType.Type.TEXT)
    private String text;

    @DbColumnName(name = TIMESTAMP)
    @DbType(type = DbType.Type.INTEGER)
    private long timestamp;

    @DbColumnName(name = PEER_ID)
    @DbType(type = DbType.Type.INTEGER)
    private long peerId;

    @DbColumnName(name = USER_ID)
    @DbType(type = DbType.Type.INTEGER)
    private long userId;

    @DbColumnName(name = IS_READ)
    @DbType(type = DbType.Type.INTEGER)
    private long isRead;

    @DbColumnName(name = IS_OUT)
    @DbType(type = DbType.Type.INTEGER)
    private long isOut;

    @DbColumnName(name = SOCIAL_NETWORK)
    @DbType(type = DbType.Type.TEXT)
    @DbPrimaryKey
    private String socialNetwork;

    @DbColumnName(name = FLAGS)
    @DbType(type = DbType.Type.INTEGER)
    private long flags;

    public String getText() {
        return text;
    }

    public long getPeerId() {
        return peerId;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    private MessageModel(long id, String text, long timestamp, long peerId, long userId, boolean isRead, boolean isOut, long flags, final SocialNetwork socialNetwork) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.peerId = peerId;
        this.userId = userId;
        this.isRead = isRead ? 1 : 0;
        this.isOut = isOut ? 1 : 0;
        this.flags = flags;
        this.socialNetwork = socialNetwork.toString();
    }

    private MessageModel(IMessage message) {
        this.id = message.getId();
        this.text = message.getText();
        this.timestamp = message.getUnixDate();
        this.peerId = message.getReceiver().getId();
        this.userId = message.getSender().getId();
        this.isRead = message.isRead() ? 1 : 0;
        this.isOut = message.isOut() ? 1 : 0;
        this.socialNetwork = message.getReceiver().getSocialNetwork().toString();
    }

    private MessageModel() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageModel) {
            final MessageModel model = (MessageModel) obj;
            return model.socialNetwork.equals(socialNetwork) && model.id == id;
        } else {
            return false;
        }
    }

    public static MessageModel getInstance() {
        return new MessageModel();
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PEER_ID, peerId);
        contentValues.put(USER_ID, userId);
        contentValues.put(TEXT, text);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(IS_READ, isRead);
        contentValues.put(IS_OUT, isOut);
        contentValues.put(FLAGS, flags);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public MessageModel convertToModel(Cursor cursor) {
        final String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {

        }
        return null;
    }

    public static List<MessageModel> convertTo(Collection<IMessage> messageCollection) {
        List<MessageModel> dialogListMessageModelList = new ArrayList<>();
        for (IMessage message : messageCollection) {
            dialogListMessageModelList.add(new MessageModel(message));
        }
        return dialogListMessageModelList;
    }

    public static List<IMessage> convertFrom(Collection<MessageModel> messageModels) {
        List<IMessage> messageList = new ArrayList<>();
        for (MessageModel messageModel : messageModels) {
            final long userId = messageModel.userId;
            final long peerId = messageModel.peerId;
            final VkMessage.Builder builder = new VkMessage.Builder().setId(messageModel.id).setText(messageModel.text).setDate(messageModel.timestamp).setOut(messageModel.isOut > 0).setRead(messageModel.isRead > 0).setSender(((ISender) VkReceiverStorage.get(userId)));
            builder.setChat(userId != peerId ? ((IChat) VkReceiverStorage.get(peerId)) : null);
            messageList.add(builder.build());
        }
        return messageList;
    }
}
