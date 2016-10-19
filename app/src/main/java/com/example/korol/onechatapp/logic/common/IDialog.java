package com.example.korol.onechatapp.logic.common;

import java.util.List;

/**
 * Created by korol on 20-Oct-16.
 */

public class IDialog {
    public ISender getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(ISender interlocutor) {
        this.interlocutor = interlocutor;
    }

    private ISender interlocutor;

    List<IMessage> messages;
}
