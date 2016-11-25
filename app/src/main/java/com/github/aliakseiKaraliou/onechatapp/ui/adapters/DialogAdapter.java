package com.github.aliakseiKaraliou.onechatapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IMessage> messageList;

    public DialogAdapter(List<IMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final IMessage currentMessage = messageList.get(position);
        final ImageLoaderManager loaderManager = new ImageLoaderManager();

        loaderManager.startLoading(currentMessage.getSender().getPhotoUrl());

        final DialogAdapterViewHolder dialogAdapterViewHolder = (DialogAdapterViewHolder) holder;
        dialogAdapterViewHolder.messageTextView.setText(currentMessage.getText());
        dialogAdapterViewHolder.photo.setImageBitmap(loaderManager.getResult());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private class DialogAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageTextView;
        private final ImageView photo;

        public DialogAdapterViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.dialog_item_message);
            photo = (ImageView) itemView.findViewById(R.id.dialog_item_primary_photo);
        }
    }
}
