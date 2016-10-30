package com.example.korol.onechatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.utils.imageLoader.OperationMemoryCache;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.adapters.VkStartMessagesScreenAdapter;
import com.example.korol.onechatapp.logic.vk.getMethods.VkGetStartScreen;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VkInfo.userGetAuth(this);
        if (VkInfo.isAuthorized()) {
            VkInfo.userSetAuth(this);
            try {
                List<IMessage> messages = VkGetStartScreen.getStartScreen();
                ListView listView = (ListView) findViewById(R.id.list_view_main_messages);
                VkStartMessagesScreenAdapter adapter = new VkStartMessagesScreenAdapter(this, messages);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long id = Long.parseLong(((TextView) view.findViewWithTag("ID")).getText().toString());
                        final String type = ((TextView) view.findViewById(R.id.start_screen_message_sender_type)).getText().toString();
                        startActivity(new Intent(MainActivity.this, ConversationActivity.class).putExtra("TYPE", type).putExtra("ID", id));
                    }
                });
                listView.setAdapter(adapter);
                final Map<Long, Bitmap> cache = new OperationMemoryCache().getCache();
                StringBuilder builder = new StringBuilder();
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

}
