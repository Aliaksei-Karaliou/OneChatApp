package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.common.Authorized;
import com.example.korol.onechatapp.logic.vk.JSON_Parser.VkStartScreenParser;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.VkRequester;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Authorized.isVkAuthorized()) {
            Toast.makeText(this, Integer.toString(VkInfo.getUserId()), Toast.LENGTH_SHORT).show();
            final VkRequester requester = new VkRequester("messages.getDialogs");
            try {
                String response = requester.execute().get();
                StringBuilder builder = new StringBuilder();
                if (!response.equals("Error request")) {
                    VkStartScreenParser parser = new VkStartScreenParser();
                    parser.parse(response);
                }
            } catch (InterruptedException | ExecutionException e) {
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
            startActivity(new Intent(this, AuthActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return super.onOptionsItemSelected(item);
    }
}
