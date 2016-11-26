package com.github.aliakseiKaraliou.onechatapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.DateFriendlyFormat;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;

import java.util.List;

public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnMessageClick onMessageClick;
    private List<IMessage> messageList;
    private final DateFriendlyFormat dateFriendlyFormat;
    private Context context;

    public DialogListAdapter(Context context, List<IMessage> messageList) {
        this.messageList = messageList;
        this.context = context;
        dateFriendlyFormat = new DateFriendlyFormat();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ReceiverType receiverType = ReceiverType.values()[viewType];
        if (receiverType == ReceiverType.USER || receiverType == ReceiverType.GROUP) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_item_user_group, parent, false);
            return new UserGroupViewHolder(view);
        } else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_item_chat, parent, false);
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
            viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));


            if (onMessageClick != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMessageClick.onClick(currentMessage.getReciever().getId());
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
            viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));

            if (onMessageClick != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMessageClick.onClick(currentMessage.getReciever().getId());
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

    public void onItemClick(OnMessageClick onMessageClick) {
        this.onMessageClick = onMessageClick;
    }

    private class UserGroupViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;
        private final TextView dateTextView;

        public UserGroupViewHolder(View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_item_primary_photo));
            dateTextView = ((TextView) itemView.findViewById(R.id.dialog_list_date));
        }
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;
        private final ImageView userPhotoImageView;
        private final TextView dateTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_item_primary_photo));
            userPhotoImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_secondary_photo));
            dateTextView = ((TextView) itemView.findViewById(R.id.dialog_list_date));
        }
    }

    public interface OnMessageClick {
        void onClick(long peerId);
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getReciever().getReceiverType().ordinal();
    }

}