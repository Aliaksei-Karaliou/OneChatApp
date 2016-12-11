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
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.DIALOGS_LIST)
public class DialogListMessageModel implements AbstractModel<DialogListMessageModel> {

    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String TIMESTAMP = "timestamp";
    private static final String PEER_ID = "peerId";
    private static final String USER_ID = "userId";
    private static final String IS_READ = "isRead";
    private static final String IS_OUT = "isOut";
    private static final String SOCIAL_NETWORK = "socialNetwork";

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
    @DbPrimaryKey
    private long peerId;

    @DbColumnName(name = USER_ID)
    @DbType(type = DbType.Type.INTEGER)
    @DbPrimaryKey
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

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getPeerId() {
        return peerId;
    }

    public long getUserId() {
        return userId;
    }

    public long getIsRead() {
        return isRead;
    }

    public long getIsOut() {
        return isOut;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    private DialogListMessageModel(long id, String text, long timestamp, long peerId, long userId, boolean isRead, boolean isOut, SocialNetwork socialNetwork) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.peerId = peerId;
        this.userId = userId;
        this.isRead = isRead ? 1 : 0;
        this.isOut = isOut ? 1 : 0;
        this.socialNetwork = socialNetwork.toString();
    }

    private DialogListMessageModel(IMessage message) {
        this.id = message.getId();
        this.text = message.getText();
        this.timestamp = message.getUnixDate();
        this.peerId = message.getReceiver().getId();
        this.userId = message.getSender().getId();
        this.isRead = message.isRead() ? 1 : 0;
        this.isOut = message.isOut() ? 1 : 0;
        this.socialNetwork = message.getReceiver().getSocialNetwork().toString();
    }

    private DialogListMessageModel() {

    }

    public static DialogListMessageModel getInstance() {
        return new DialogListMessageModel();
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
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public DialogListMessageModel convertToModel(Cursor cursor) {
        final long id = cursor.getLong(0);
        final long isOut = cursor.getLong(1);
        final long isRead = cursor.getLong(2);
        final long peerId = cursor.getLong(3);
        final String socialNetwork = cursor.getString(4);
        final String text = cursor.getString(5);
        final long timestamp = cursor.getLong(6);
        final long userId = cursor.getLong(7);

        return new DialogListMessageModel(id, text, timestamp, peerId, userId, isRead > 0, isOut > 0, SocialNetwork.valueOf(socialNetwork));
    }

    public static List<DialogListMessageModel> convertTo(Collection<IMessage> messageCollection) {
        List<DialogListMessageModel> dialogListMessageModelList = new ArrayList<>();
        for (IMessage message : messageCollection) {
            dialogListMessageModelList.add(convertTo(message));
        }
        return dialogListMessageModelList;
    }

    public static DialogListMessageModel convertTo(IMessage message) {
        return new DialogListMessageModel(message);
    }

    public static List<IMessage> convertFrom(Collection<DialogListMessageModel> dialogListMessageModels) {
        List<IMessage> messageList = new ArrayList<>();
        IMessage message;
        for (DialogListMessageModel dialogListMessageModel : dialogListMessageModels) {
            final long userId = dialogListMessageModel.userId;
            final long peerId = dialogListMessageModel.peerId;
            final VkMessage.Builder builder = new VkMessage.Builder().setId(dialogListMessageModel.id).setText(dialogListMessageModel.text).setDate(dialogListMessageModel.timestamp).setOut(dialogListMessageModel.isOut > 0).setRead(dialogListMessageModel.isRead > 0).setSender(((ISender) VkReceiverStorage.get(userId)));
            builder.setChat(userId != peerId ? ((IChat) VkReceiverStorage.get(peerId)) : null);
            messageList.add(builder.build());
        }
        return messageList;
    }
}
