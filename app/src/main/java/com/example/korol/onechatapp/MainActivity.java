package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.broadcastReceiver.MessageReceiver;
import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.common.adapters.StartMessagesScreenAdapter;
import com.example.korol.onechatapp.logic.vk.VkRequester;
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
                final ListView listView = (ListView) findViewById(R.id.activity_main_list_view_main_messages);
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            offset += 20;
                            if (listView.getId() == R.id.activity_main_list_view_main_messages) {
                                try {
                                    List list = VkGetStartScreen.getStartScreen(offset);
                                    final StringBuilder builder = new StringBuilder();
                                } catch (AccessTokenException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                StartMessagesScreenAdapter adapter = new StartMessagesScreenAdapter(this, messages);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String senderPacker = ((TextView) view.findViewById(R.id.start_screen_message_sender_packer)).getText().toString();
                        final String name = ((TextView) view.findViewById(R.id.start_screen_message_name)).getText().toString();
                        startActivity(new Intent(MainActivity.this, ConversationActivity.class).putExtra("Sender Packer", senderPacker));
                    }
                });
                listView.setAdapter(adapter);
                //   final Map<Long, Bitmap> cache = new OperationMemoryCache().getCache();
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
