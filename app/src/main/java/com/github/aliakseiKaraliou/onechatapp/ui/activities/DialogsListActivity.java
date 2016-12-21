package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.managers.VkDialogsListManager;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogListAdapter;

import java.util.List;

public class DialogsListActivity extends AppCompatActivity {

    private List<IMessage> messages;
    private BroadcastReceiver broadcastReceiver;
    private SharedPreferences preferences;
    private DialogListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs_list);
        preferences = ((App) getApplicationContext()).getApplicationSharedPreferences();

        if (VkInfo.isUserAuthorized()) {
            auth();
            broadcastReceiver = new NewEventBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VkInfo.userSetAuth(preferences);
        if (VkInfo.isUserAuthorized() && messages == null) {
            auth();
        }
    }

    private void auth() {
        final VkDialogsListManager vkDialogsListManager = new VkDialogsListManager();
        vkDialogsListManager.startLoading(DialogsListActivity.this, 0);

        final RecyclerView messagesRecyclerView = (RecyclerView) findViewById(R.id.activity_dialogs_list_messages);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        messages = vkDialogsListManager.getResult();
        vkDialogsListManager.startLoading(DialogsListActivity.this, 20);


        adapter = new DialogListAdapter(this, messages);
        messagesRecyclerView.setAdapter(adapter);

        adapter.onItemClick(new DialogListAdapter.OnMessageClick() {
            @Override
            public void onClick(long peerId) {
                final Intent intent = new Intent(DialogsListActivity.this, DialogActivity.class);
                intent.putExtra(Constants.Other.PEER_ID, peerId);
                startActivity(intent);
            }
        });

        messagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItem = layoutManager.getItemCount();

                if (lastVisibleItem + 1 == totalItem && dy > 0) {

                    List<IMessage> result = vkDialogsListManager.getResult();

                    if (result != null) {

                        vkDialogsListManager.startLoading(DialogsListActivity.this, totalItem);

                        assert messages != null;
                        messages.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dialog_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dialog_list_item_authorization) {
            startActivity(new Intent(this, AuthActivity.class));
        } else if (item.getItemId() == R.id.dialog_list_preferences) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private class NewEventBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final List<IEvent> eventList = intent.getParcelableArrayListExtra(Constants.Other.EVENT_LIST);
            for (IEvent event : eventList) {
                if (event instanceof IMessage) {
                    final IMessage currentBroadcastMessage = (IMessage) event;
                    IReceiver receiver = currentBroadcastMessage.getReceiver();
                    for (int i = 0; i < messages.size(); i++) {
                        IMessage message = messages.get(i);
                        if (receiver.isEquals(message.getReceiver())) {
                            messages.remove(i);
                            break;
                        }
                    }
                    messages.add(0, currentBroadcastMessage);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
