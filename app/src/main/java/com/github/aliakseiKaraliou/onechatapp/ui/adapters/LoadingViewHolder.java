package com.github.aliakseiKaraliou.onechatapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.aliakseiKaraliou.onechatapp.R;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public LoadingViewHolder(final View itemView) {
        super(itemView);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
        final TextView textView = (TextView) itemView.findViewById(R.id.loading_textView);
    }
}
