package com.example.korol.onechatapp.logic.vk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.utils.imageLoader.ImageLoader;
import com.example.korol.onechatapp.logic.vk.entities.VkChat;
import com.example.korol.onechatapp.logic.vk.entities.VkSenderType;
import com.example.korol.onechatapp.logic.vk.entities.VkUser;

import java.util.List;
import java.util.Locale;

public class VkStartMessagesScreenAdapter extends BaseAdapter {

    private final List<IMessage> messageList;

    private final LayoutInflater inflater;

    private View.OnClickListener onClickListener;

    public VkStartMessagesScreenAdapter(Context context, List<IMessage> messageList) {
        this.messageList = messageList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

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
        final ImageLoader imageLoader = new ImageLoader();
        if (view == null)
            view = inflater.inflate(R.layout.start_screen_message_item, viewGroup, false);

        IMessage currentMessage = messageList.get(i);

        final VkSenderType senderType = getSenderType(currentMessage.getSender());

        ((ImageView) view.findViewById(R.id.start_screen_message_avatar)).setImageBitmap(imageLoader.getBitmapFromUrl(currentMessage.getSender().getPhotoUrl()));
        ((TextView) view.findViewById(R.id.start_screen_message_message)).setText(currentMessage.getText());
        ((TextView) view.findViewById(R.id.start_screen_message_name)).setText(currentMessage.getSender().getName());
        ((TextView) view.findViewById(R.id.start_screen_message_id)).setText(String.format(Locale.getDefault(), "%d", currentMessage.getSender().getId()));
        ((TextView) view.findViewById(R.id.start_screen_message_sender_type)).setText(senderType.toString());

        if (onClickListener != null)
            view.findViewById(R.id.start_screen_message_item).setOnClickListener(onClickListener);
        return view;
    }

    private VkSenderType getSenderType(ISender sender) {

        if (sender instanceof VkUser)
            return VkSenderType.USER;
        else if (sender instanceof VkChat)
            return VkSenderType.CHAT;
        else
            return VkSenderType.GROUP;
    }
}
