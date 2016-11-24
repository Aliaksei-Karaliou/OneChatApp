package com.github.aliakseiKaraliou.onechatapp.ui.recyclerView.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;

import java.util.List;

public class DialogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IMessage> messages;

    public DialogRecyclerAdapter(IDialog dialog) {
        this.messages = dialog.getMessages();
    }

    public DialogRecyclerAdapter(List<IMessage> messages) {
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DialogRecyclerViewHolder viewHolder = (DialogRecyclerViewHolder) holder;
        final IMessage currentMessage = messages.get(position);
     //   final ImageLoader imageLoader = new ImageLoader();

        //   viewHolder.avatarLeftImageView.setImageBitmap(imageLoader.loadFromUrl(currentMessage.getSender().getPhotoUrl()));
        viewHolder.messageTextView.setText(currentMessage.getText());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class DialogRecyclerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatarLeftImageView;
        private final TextView messageTextView;

        public DialogRecyclerViewHolder(View itemView) {
            super(itemView);
            avatarLeftImageView = ((ImageView) itemView.findViewById(R.id.dialog_item_left_avatar));
            messageTextView = ((TextView) itemView.findViewById(R.id.dialog_item_message));
        }
    }

}
