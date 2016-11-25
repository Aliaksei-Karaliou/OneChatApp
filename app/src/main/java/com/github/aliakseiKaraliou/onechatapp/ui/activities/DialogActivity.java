package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.managers.VkDialogManager;

import java.util.List;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Intent intent = getIntent();
        long peerId = intent.getLongExtra(VkConstants.Extra.PEER_ID, -1);

        IReciever reciever = null;
        if (peerId > 0) {
            reciever = VkReceiverStorage.get(peerId);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(reciever.getName());
        }

        VkDialogManager manager = new VkDialogManager();
        manager.startLoading(reciever, 0);

        final List<IMessage> result = manager.getResult();
        new StringBuilder();
    }
}
