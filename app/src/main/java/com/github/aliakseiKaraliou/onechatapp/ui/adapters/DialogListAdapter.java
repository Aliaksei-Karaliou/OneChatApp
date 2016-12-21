package com.github.aliakseiKaraliou.onechatapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.DateFriendlyFormat;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.LazyImageLoaderManager;

import java.util.List;

public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View.OnClickListener onMessageClick;
    private List<IMessage> messageList;
    private final DateFriendlyFormat dateFriendlyFormat;
    private Context context;
    private Bitmap defaultBitmap;

    public DialogListAdapter(Context context, List<IMessage> messageList) {
        this.messageList = messageList;
        this.context = context;
        dateFriendlyFormat = new DateFriendlyFormat();
        this.defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_50);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ReceiverType receiverType = ReceiverType.values()[viewType];
        if (receiverType != ReceiverType.CHAT) {
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
        final LazyImageLoaderManager loaderManager = ((App) context.getApplicationContext()).getImageLoaderManager();
        final int viewType = getItemViewType(position);
        final ReceiverType receiverType = ReceiverType.values()[viewType];

        if (receiverType != ReceiverType.CHAT) {

            final UserGroupViewHolder viewHolder = (UserGroupViewHolder) holder;

            loaderManager.load(context, viewHolder.avatarImageView, currentMessage.getReceiver().getPhoto50Url(), defaultBitmap);

            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.nameTextView.setText(currentMessage.getSender().getName());
            viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));


            if (onMessageClick != null) {
                viewHolder.itemView.setTag(currentMessage.getReceiver().getId());
                viewHolder.itemView.setOnClickListener(onMessageClick);
            }


        } else {


            final ChatViewHolder viewHolder = (ChatViewHolder) holder;
            loaderManager.load(context, viewHolder.avatarImageView, currentMessage.getReceiver().getPhoto50Url(), defaultBitmap);
            loaderManager.load(context, viewHolder.userPhotoImageView, currentMessage.getSender().getPhoto50Url(), defaultBitmap);


            viewHolder.messageTextView.setText(currentMessage.getText());
            viewHolder.nameTextView.setText(currentMessage.getReceiver().getName());
            viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));

            if (onMessageClick != null) {
                viewHolder.itemView.setTag(currentMessage.getReceiver().getId());
                viewHolder.itemView.setOnClickListener(onMessageClick);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void onItemClick(final OnMessageClick onMessageClick) {
        this.onMessageClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long tag = (long) view.getTag();
                onMessageClick.onClick(tag);
            }
        };
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
        return messageList.get(position).getReceiver().getReceiverType().ordinal();
    }

}