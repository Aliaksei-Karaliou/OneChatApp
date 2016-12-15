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
import android.widget.Toast;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.DateFriendlyFormat;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.LazyImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IMessage> messageList;
    private Context context;
    private Bitmap defaultBitmap;

    public DialogAdapter(Context context, List<IMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_50);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final IMessage currentMessage = messageList.get(position);
        final LazyImageLoaderManager loaderManager = ((App) context.getApplicationContext()).getImageLoaderManager();

        final DialogAdapterViewHolder dialogAdapterViewHolder = (DialogAdapterViewHolder) holder;
        final String photoUrl;
        if (currentMessage.isOut()) {
            photoUrl = VkReceiverStorage.get(VkInfo.getUserId()).getPhoto50Url();
        } else {
            photoUrl = currentMessage.getSender().getPhoto50Url();
        }
        loaderManager.load(context, dialogAdapterViewHolder.photo, photoUrl, defaultBitmap);
        dialogAdapterViewHolder.messageTextView.setText(currentMessage.getText());
        dialogAdapterViewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, currentMessage.getSender().getName(), Toast.LENGTH_SHORT).show();
            }
        });
        dialogAdapterViewHolder.timeTexView.setText(new DateFriendlyFormat().convert(context, currentMessage.getDate()));
    }

    public void deleteItem(int position) {
        messageList.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private class DialogAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageTextView;
        private final TextView timeTexView;
        private final ImageView photo;

        public DialogAdapterViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.dialog_item_message);
            timeTexView = (TextView) itemView.findViewById(R.id.dialog_item_time);
            photo = (ImageView) itemView.findViewById(R.id.dialog_item_primary_photo);
        }
    }

}
