package com.example.korol.onechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.getMethods.VkGetStartScreen;
import com.example.korol.onechatapp.ui.adapters.StartScreenMessagesAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int offset = 0;
    private static List<IMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VkInfo.userGetAuth(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (VkInfo.isAuthorized()) {
            VkInfo.userSetAuth(this);
            try {
                messages = VkGetStartScreen.getStartScreen();
                final RecyclerView messagesRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view_main_messages);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                messagesRecyclerView.setLayoutManager(layoutManager);
                final StartScreenMessagesAdapter adapter = new StartScreenMessagesAdapter(this.messages);
                adapter.onItemClick(new StartScreenMessagesAdapter.OnMessageClick() {
                    @Override
                    public void onClick(ISender sender) {
                        Intent intent = new Intent(MainActivity.this, ConversationActivity.class).putExtra("Sender", sender);
                        startActivity(intent);
                    }
                });
                messagesRecyclerView.setAdapter(adapter);
            } catch (AccessTokenException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.authorization_menu)
            startActivity(new Intent(this, AuthActivity.class));
        return super.onOptionsItemSelected(item);
    }
    //Broadcast Receiver
}
