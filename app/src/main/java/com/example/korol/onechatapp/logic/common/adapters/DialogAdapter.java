package com.example.korol.onechatapp.logic.common.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.imageLoader.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public DialogAdapter(Context context, @NonNull IDialog dialog) {
        this.messageList = new ArrayList<>(dialog.getMessages());
        Collections.reverse(this.messageList);
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
            view = inflater.inflate(R.layout.dialog_item, viewGroup, false);

        IMessage currentMessage = messageList.get(i);
        ImageLoader loader = new ImageLoader();

        String message = currentMessage.getText();

        final TextView viewById = (TextView) view.findViewById(R.id.dialog_item_message);
        viewById.setText(message);

        ((ImageView) view.findViewById(R.id.dialog_item_avatar)).setImageBitmap(loader.getBitmapFromUrl(currentMessage.getSender().getPhotoUrl()));
        
        return view;
    }
}
