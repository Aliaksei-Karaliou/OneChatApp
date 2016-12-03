package com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.util.ArrayList;
import java.util.List;

public class DbReceiver implements DbConvert {

    @DbPrimaryKey
    @DbType(type = DbType.Type.INTEGER)
    private long receiverId;
    @DbType(type = DbType.Type.TEXT)
    private String firstName;
    @DbType(type = DbType.Type.TEXT)
    private String secondName;
    @DbType(type = DbType.Type.TEXT)
    private String photoUrl;
    @DbType(type = DbType.Type.TEXT)
    private String socialNetwork;
    @DbType(type = DbType.Type.TEXT)
    private String type;

    private DbReceiver(long receiverId, String firstName, String secondName, java.lang.String photoUrl, String socialNetwork, String type) {
        this.receiverId = receiverId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.photoUrl = photoUrl;
        this.socialNetwork = socialNetwork;
        this.type = type;
    }

    public static List<DbReceiver> convert(List<IReciever> recieverList) {
        List<DbReceiver> dbReceivers = new ArrayList<>();
        for (IReciever reciever : recieverList) {
            final long receiverId = reciever.getId();
            final String photoUrl = reciever.getPhotoUrl();
            final String socialNetwork = reciever.getSocialNetwork().toString();
            final String receiverType = reciever.getPeerReceiverType().toString();
            java.lang.String firstName = "", secondName = "";
            if (receiverType.equals(PeerRecieverType.USER.toString())) {
                final IUser user = (IUser) reciever;
                firstName = user.getFirstName();
                secondName = user.getLastName();
            } else {
                secondName = reciever.getName();
            }
            DbReceiver receiver = new DbReceiver(receiverId, firstName, secondName, photoUrl, socialNetwork, receiverType);
            dbReceivers.add(receiver);
        }
        return dbReceivers;
    }

    @Override
    public DbConvert convert(String[] values) {
        return null;
    }
}
