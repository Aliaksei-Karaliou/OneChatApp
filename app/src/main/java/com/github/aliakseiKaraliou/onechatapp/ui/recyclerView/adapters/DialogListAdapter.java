package com.github.aliakseiKaraliou.onechatapp.ui.recyclerView.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;

import java.util.List;

public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public void onItemClick(OnMessageClick onMessageClick) {
        this.onMessageClick = onMessageClick;
    }

    private OnMessageClick onMessageClick;

    public DialogListAdapter(List<IMessage> messageList) {
        this.messageList = messageList;
    }

    private List<IMessage> messageList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ReceiverType receiverType = ReceiverType.values()[viewType];
        if (receiverType == ReceiverType.USER || receiverType == ReceiverType.GROUP) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_user_group_item, parent, false);
            return new UserGroupViewHolder(view);
        } else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_chat_item, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final IMessage currentMessage = messageList.get(position);
        final ImageLoaderManager loaderManager = new ImageLoaderManager();
        final int viewType = getItemViewType(position);
        final ReceiverType receiverType = ReceiverType.values()[viewType];

        if (receiverType == ReceiverType.USER || receiverType == ReceiverType.GROUP) {
            loaderManager.startLoading(currentMessage.getSender().getPhotoUrl());
            final UserGroupViewHolder viewHolder = (UserGroupViewHolder) holder;
            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.nameTextView.setText(currentMessage.getSender().getName());
            if (onMessageClick != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMessageClick.onClick(currentMessage.getReciever());
                    }
                });
            }
            viewHolder.avatarImageView.setImageBitmap(loaderManager.getResult());
        } else {
            loaderManager.startLoading(currentMessage.getSender().getPhotoUrl());
            final ChatViewHolder viewHolder = (ChatViewHolder) holder;
            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.userPhotoImageView.setImageBitmap(loaderManager.getResult());
            loaderManager.startLoading(currentMessage.getReciever().getPhotoUrl());
            viewHolder.nameTextView.setText(currentMessage.getReciever().getName());
            if (onMessageClick != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMessageClick.onClick(currentMessage.getReciever());
                    }
                });
            }
            viewHolder.avatarImageView.setImageBitmap(loaderManager.getResult());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private class UserGroupViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;

        public UserGroupViewHolder(View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_photo));
        }
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;
        private final ImageView userPhotoImageView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_photo));
            userPhotoImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_user_photo));
        }
    }

    public interface OnMessageClick {
        void onClick(IReciever reciever);
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getReciever().getReceiverType().ordinal();
    }

}
