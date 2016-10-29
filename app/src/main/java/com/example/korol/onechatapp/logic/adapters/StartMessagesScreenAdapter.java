package com.example.korol.onechatapp.logic.adapters;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.imageLoader.ImageLoader;

import java.util.List;

public class StartMessagesScreenAdapter extends BaseAdapter {

    private final List<IMessage> messageList;

    private final LayoutInflater inflater;

    public StartMessagesScreenAdapter(Context context, List<IMessage> messageList) {
        this.messageList = messageList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final StringBuilder builder = new StringBuilder();
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

        ((ImageView) view.findViewById(R.id.start_screen_message_avatar)).setImageBitmap(imageLoader.getBitmapFromUrl(currentMessage.getSender().getPhotoUrl()));
        ((TextView) view.findViewById(R.id.start_screen_message_message)).setText(currentMessage.getText());
        ((TextView) view.findViewById(R.id.start_screen_message_name)).setText(currentMessage.getSender().getName());

        return view;
    }
}
