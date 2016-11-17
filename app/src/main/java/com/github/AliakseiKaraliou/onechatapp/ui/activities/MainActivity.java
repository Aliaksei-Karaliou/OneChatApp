package com.github.AliakseiKaraliou.onechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.AliakseiKaraliou.onechatapp.R;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.network.NetworkConnectionChecker;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.getMethods.VkGetStartScreen;
import com.github.AliakseiKaraliou.onechatapp.ui.adapters.StartScreenMessagesAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static boolean isOnline = false;
    private RecyclerView messagesRecyclerView;
    private List<IMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (NetworkConnectionChecker.check(this)) {
            isOnline = true;
            VkInfo.userGetAuth(this);
        }
        this.messagesRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view_main_messages);
        final RecyclerView messagesRecyclerView = this.messagesRecyclerView;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (dy > 0 && lastVisibleItemPosition + 1 == totalItemCount) {
                    try {
                        final List<IMessage> loadedMessages = VkGetStartScreen.getStartScreen(MainActivity.this, messages.size());
                        assert loadedMessages != null;
                        messages.addAll(loadedMessages);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } catch (AccessTokenException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, R.string.error_access_token, Toast.LENGTH_SHORT).show();
                    }
                }
                assert getSupportActionBar() != null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOnline)
            Toast.makeText(this, "Offline", Toast.LENGTH_SHORT).show();
        else if (VkInfo.isAuthorized()) {
            VkInfo.userSetAuth(this);
            try {
                this.messages = VkGetStartScreen.getStartScreen(this);
                final StartScreenMessagesAdapter adapter = new StartScreenMessagesAdapter(messages);
                adapter.onItemClick(new StartScreenMessagesAdapter.OnMessageClick() {
                    @Override
                    public void onClick(ISender sender) {
                        startActivity(new Intent(MainActivity.this, ConversationActivity.class).putExtra("Sender", sender));
                    }
                });
                messagesRecyclerView.setAdapter(adapter);
            } catch (AccessTokenException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.error_access_token, Toast.LENGTH_SHORT).show();
            }
        }
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
