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
    private final List<IMessage> messageList;
    private final DateFriendlyFormat dateFriendlyFormat;
    private final Context context;
    private final Bitmap defaultBitmap;
    private static final int LOADING_VIEW_TYPE = 4;


    public DialogListAdapter(final Context context, final List<IMessage> messageList) {
        this.messageList = messageList;
        this.context = context;
        dateFriendlyFormat = new DateFriendlyFormat();
        this.defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_50);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == LOADING_VIEW_TYPE) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            final ReceiverType receiverType = ReceiverType.values()[viewType];
            if (receiverType != ReceiverType.CHAT) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_item_user_group, parent, false);
                return new UserGroupViewHolder(view);
            } else {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_item_chat, parent, false);
                return new ChatViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final int viewType = getItemViewType(position);

        if (viewType != LOADING_VIEW_TYPE) {
            final IMessage currentMessage = messageList.get(position);
            final LazyImageLoaderManager loaderManager = ((App) context.getApplicationContext()).getImageLoaderManager();
            final ReceiverType receiverType = ReceiverType.values()[viewType];

            if (receiverType != ReceiverType.CHAT) {

                final UserGroupViewHolder viewHolder = (UserGroupViewHolder) holder;

                loaderManager.load(context, viewHolder.avatarImageView, currentMessage.getReceiver().getPhoto50Url(), defaultBitmap);

                viewHolder.messageTextView.setText(currentMessage.getText());
                viewHolder.nameTextView.setText(currentMessage.getSender().getName());
                viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));

                if (onMessageClick != null) {
                    holder.itemView.setTag(currentMessage.getReceiver().getId());
                    holder.itemView.setOnClickListener(onMessageClick);
                }


                if (!currentMessage.isRead()) {
                    if (!currentMessage.isOut()) {
                        holder.itemView.setBackgroundColor(BackgroundColoursConstants.ITEM_UNREAD_BACKGROUND);
                    } else {
                        viewHolder.readState.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.itemView.setBackgroundColor(BackgroundColoursConstants.ITEM_READ_BACKGROUND);
                    viewHolder.readState.setVisibility(View.INVISIBLE);
                }


            } else {


                final ChatViewHolder viewHolder = (ChatViewHolder) holder;
                loaderManager.load(context, viewHolder.avatarImageView, currentMessage.getReceiver().getPhoto50Url(), defaultBitmap);
                loaderManager.load(context, viewHolder.userPhotoImageView, currentMessage.getSender().getPhoto50Url(), defaultBitmap);


                viewHolder.messageTextView.setText(currentMessage.getText());
                viewHolder.nameTextView.setText(currentMessage.getReceiver().getName());
                viewHolder.dateTextView.setText(dateFriendlyFormat.convert(context, currentMessage.getDate()));

                if (onMessageClick != null) {
                    holder.itemView.setTag(currentMessage.getReceiver().getId());
                    holder.itemView.setOnClickListener(onMessageClick);
                }

                if (!currentMessage.isRead()) {
                    if (!currentMessage.isOut()) {
                        holder.itemView.setBackgroundColor(BackgroundColoursConstants.ITEM_UNREAD_BACKGROUND);
                    } else {
                        viewHolder.readState.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.itemView.setBackgroundColor(BackgroundColoursConstants.ITEM_READ_BACKGROUND);
                    viewHolder.readState.setVisibility(View.INVISIBLE);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return messageList.size() + 1;
    }

    public void onItemClick(final OnMessageClick onMessageClick) {
        this.onMessageClick = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
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
        private final ImageView readState;

        public UserGroupViewHolder(final View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_primary_photo));
            dateTextView = ((TextView) itemView.findViewById(R.id.dialog_list_date));
            readState = ((ImageView) itemView.findViewById(R.id.dialog_list_read_state));
        }
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView messageTextView;
        private final ImageView avatarImageView;
        private final ImageView userPhotoImageView;
        private final TextView dateTextView;
        private final ImageView readState;

        public ChatViewHolder(final View itemView) {
            super(itemView);
            nameTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_name));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_list_item_message));
            avatarImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_primary_photo));
            userPhotoImageView = ((ImageView) itemView.findViewById(R.id.dialog_list_secondary_photo));
            dateTextView = ((TextView) itemView.findViewById(R.id.dialog_list_date));
            readState = ((ImageView) itemView.findViewById(R.id.dialog_list_read_state));
        }
    }

    public interface OnMessageClick {
        void onClick(long peerId);
    }

    @Override
    public int getItemViewType(final int position) {
        return position == messageList.size() ? LOADING_VIEW_TYPE : messageList.get(position).getReceiver().getReceiverType().ordinal();
    }

}