package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.common.adapters.StartMessagesRecyclerViewAdapter;
import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.getMethods.VkGetStartScreen;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int offset = 0;

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
                List<IMessage> messages = VkGetStartScreen.getStartScreen();
                if (messages == null)
                    throw new AccessTokenException();
                final RecyclerView messagesRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view_main_messages);
                messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                final StartMessagesRecyclerViewAdapter adapter = new StartMessagesRecyclerViewAdapter(messages);
                adapter.setOnMessageClick(new StartMessagesRecyclerViewAdapter.OnMessageClick() {
                    @Override
                    public void onClick(ISender sender) {
                        Intent intent = new Intent(MainActivity.this, ConversationActivity.class).putExtra("Sender", sender);
                        startActivity(intent);
                    }
                });
                messagesRecyclerView.setAdapter(adapter);
            } catch (AccessTokenException e) {
                Toast.makeText(this, getString(R.string.access_token_error), Toast.LENGTH_SHORT).show();
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
