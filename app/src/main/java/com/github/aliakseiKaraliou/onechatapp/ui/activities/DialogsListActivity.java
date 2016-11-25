package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.network.NetworkConnectionChecker;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.managers.VkDialogsListManager;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogListAdapter;

import java.util.List;

public class DialogsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs_list);
        if (NetworkConnectionChecker.check(this)) {
            boolean isOnline = true;
            VkInfo.userGetAuth(this);
        }

        String token=VkInfo.getAccessToken();
        if (VkInfo.isUserAuthorized()) {

            final VkDialogsListManager vkDialogsListManager = new VkDialogsListManager();
            vkDialogsListManager.startLoading(0);

            final RecyclerView messagesRecyclerView = (RecyclerView) findViewById(R.id.activity_dialogs_list_messages);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            messagesRecyclerView.setLayoutManager(layoutManager);
            messagesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            final List<IMessage> messages = vkDialogsListManager.getResult();
            vkDialogsListManager.startLoading(20);

            final DialogListAdapter adapter = new DialogListAdapter(messages);
            messagesRecyclerView.setAdapter(adapter);

            adapter.onItemClick(new DialogListAdapter.OnMessageClick() {
                @Override
                public void onClick(long peerId) {
                    final Intent intent = new Intent(DialogsListActivity.this, DialogActivity.class);
                    intent.putExtra(VkConstants.Extra.PEER_ID, peerId);
                    startActivity(intent);
                }
            });

            final ImageLoaderManager manager = new ImageLoaderManager();
            messagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int lastVisibleItem=layoutManager.findLastVisibleItemPosition();
                    int totalItem=layoutManager.getItemCount();

                    if (lastVisibleItem + 1 == totalItem && dy > 0) {

                        List<IMessage> result=vkDialogsListManager.getResult();

                        //preload images
                        for (IMessage iMessage : result) {
                            manager.startLoading(iMessage.getReciever().getPhotoUrl());
                            manager.getResult();
                            if (!iMessage.getReciever().equals(iMessage.getSender())) {
                                manager.startLoading(iMessage.getSender().getPhotoUrl());
                                manager.getResult();
                            }
                        }

                        messages.addAll(result);
                        adapter.notifyDataSetChanged();


                        vkDialogsListManager.startLoading(totalItem);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token=VkInfo.getAccessToken();
        VkInfo.userSetAuth(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.start_screen_menu_item_authorization)
            startActivity(new Intent(this, AuthActivity.class));
        else if (item.getItemId() == R.id.start_screen_menu_item_refresh)
            super.onResume();
        return super.onOptionsItemSelected(item);
    }
    //Broadcast Receiver
}
