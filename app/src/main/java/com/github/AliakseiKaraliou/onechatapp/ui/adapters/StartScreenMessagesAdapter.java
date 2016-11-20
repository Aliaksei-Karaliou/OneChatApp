package com.github.AliakseiKaraliou.onechatapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.AliakseiKaraliou.onechatapp.R;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.AliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoader;

import java.util.List;

public class StartScreenMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public void onItemClick(OnMessageClick onMessageClick) {
        this.onMessageClick = onMessageClick;
    }

    private OnMessageClick onMessageClick;

    public StartScreenMessagesAdapter(List<IMessage> messageList) {
        this.messageList = messageList;
    }

    private List<IMessage> messageList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ReceiverType receiverType = ReceiverType.values()[viewType];
        if (receiverType == ReceiverType.USER || receiverType == ReceiverType.GROUP) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_screen_message_user_group_item, parent, false);
            return new UserGroupViewHolder(view);
        } else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_screen_message_chat_item, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final IMessage currentMessage = messageList.get(position);
        final ImageLoader loader = new ImageLoader();
        final int viewType = getItemViewType(position);
        final ReceiverType receiverType = ReceiverType.values()[viewType];

        if (receiverType == ReceiverType.USER || receiverType == ReceiverType.GROUP) {
            final UserGroupViewHolder viewHolder = (UserGroupViewHolder) holder;
            viewHolder.avatarImageView.setImageBitmap(loader.getBitmapFromUrl(currentMessage.getSender().getPhotoUrl()));
            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.nameTextView.setText(currentMessage.getSender().getName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMessageClick.onClick(currentMessage.getReciever());
                }
            });
        } else {
            final ChatViewHolder viewHolder = (ChatViewHolder) holder;
            viewHolder.avatarImageView.setImageBitmap(loader.getBitmapFromUrl(currentMessage.getReciever().getPhotoUrl()));
            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.nameTextView.setText(currentMessage.getReciever().getName());
            viewHolder.userPhotoImageView.setImageBitmap(loader.getBitmapFromUrl(currentMessage.getSender().getPhotoUrl()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMessageClick.onClick(currentMessage.getReciever());
                }
            });

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
            nameTextView = ((TextView) itemView.findViewById(R.id.start_screen_message_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.start_screen_message_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.start_screen_message_avatar));
        }
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;
        private final ImageView userPhotoImageView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.start_screen_message_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.start_screen_message_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.start_screen_message_avatar));
            userPhotoImageView = ((ImageView) itemView.findViewById(R.id.start_screen_chat_message_user_photo));
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
