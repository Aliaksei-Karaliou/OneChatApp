package com.github.aliakseiKaraliou.onechatapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.DateFriendlyFormat;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.AvatarImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<IMessage> messageList;
    private final Context context;
    private final Bitmap defaultBitmap;
    private final View.OnClickListener onClickListener;

    private static final int DATA_TYPE = 0;
    private static final int LOADING_TYPE = 1;

    private static final int ITEM_UNREAD_BACKGROUND = Color.rgb(221, 221, 221);


    public DialogAdapter(final Context context, final List<IMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_50);
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final View parent = (View) view.getParent();
                Toast.makeText(context, parent.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == DATA_TYPE) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
            return new DialogAdapterViewHolder(view);
        } else if (viewType == LOADING_TYPE) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final int viewType = getItemViewType(position);

        if (viewType == DATA_TYPE) {

            final IMessage currentMessage = messageList.get(position);
            final AvatarImageLoaderManager loaderManager = ((App) context.getApplicationContext()).getImageLoaderManager();

            final DialogAdapterViewHolder dialogAdapterViewHolder = (DialogAdapterViewHolder) holder;
            final IReceiver receiver;
            if (currentMessage.isOut()) {
                receiver = VkReceiverStorage.get(VkInfo.getUserId());
            } else {
                receiver = currentMessage.getSender();
            }
            loaderManager.load(context, dialogAdapterViewHolder.photo, receiver.getPhoto50Url(), defaultBitmap);
            dialogAdapterViewHolder.messageTextView.setText(currentMessage.getText());

            dialogAdapterViewHolder.photo.setOnClickListener(onClickListener);
            dialogAdapterViewHolder.itemView.setTag(receiver.getName());
            dialogAdapterViewHolder.timeTexView.setText(new DateFriendlyFormat().convert(context, currentMessage.getDate()));

            if (!currentMessage.isRead()) {
                if (!currentMessage.isOut()) {
                    dialogAdapterViewHolder.outputReadState.setVisibility(View.VISIBLE);
                    dialogAdapterViewHolder.inputReadState.setVisibility(View.INVISIBLE);
                } else {
                    dialogAdapterViewHolder.inputReadState.setVisibility(View.VISIBLE);

                }
            } else {
                dialogAdapterViewHolder.outputReadState.setVisibility(View.INVISIBLE);
                dialogAdapterViewHolder.inputReadState.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size() + 1;
    }

    @Override
    public int getItemViewType(final int position) {
        return position == messageList.size() ? LOADING_TYPE : DATA_TYPE;
    }

    private class DialogAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageTextView;
        private final TextView timeTexView;
        private final ImageView photo;
        private final ImageView inputReadState;
        private final ImageView outputReadState;

        public DialogAdapterViewHolder(final View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.dialog_item_message);
            timeTexView = (TextView) itemView.findViewById(R.id.dialog_item_time);
            photo = (ImageView) itemView.findViewById(R.id.dialog_item_primary_photo);
            inputReadState = ((ImageView) itemView.findViewById(R.id.dialog_list_output_read_state));
            outputReadState = ((ImageView) itemView.findViewById(R.id.dialog_list_input_read_state));
        }
    }

}
