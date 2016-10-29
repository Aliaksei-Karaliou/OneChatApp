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
import android.widget.Toast;

import com.example.korol.onechatapp.logic.utils.adapters.StartMessagesScreenAdapter;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.utils.imageLoader.OperationMemoryCache;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.getMethods.GetStartScreen;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<IMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VkInfo.userGetAuth(this);
        messages = ((List<IMessage>) getLastNonConfigurationInstance());
        final StringBuilder builder = new StringBuilder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VkInfo.isAuthorized()) {
            VkInfo.userSetAuth(this);
            try {
                messages = GetStartScreen.getStartScreen();
                ListView listView = (ListView) findViewById(R.id.list_view_main_messages);
                StartMessagesScreenAdapter adapter = new StartMessagesScreenAdapter(this, messages);
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
            startActivity(new Intent(this, AuthActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return messages;
    }


}
