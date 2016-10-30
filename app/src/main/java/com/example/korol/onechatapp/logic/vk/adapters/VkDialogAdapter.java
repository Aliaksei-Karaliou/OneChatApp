package com.example.korol.onechatapp.logic.vk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.IMessage;

import java.util.List;

public class VkDialogAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public VkDialogAdapter(Context context, @NonNull IDialog dialog) {
        this.messageList = dialog.getMessages();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private List<IMessage> messageList;


    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.start_screen_message_item, viewGroup, false);

        IMessage currentMessage = messageList.get(i);

        String message=currentMessage.getText();


        final TextView viewById = (TextView) view.findViewById(R.id.dialog_item_message);
        viewById.setText(message);
        final StringBuilder builder = new StringBuilder();
        return view;
    }
}
