package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.korol.onechatapp.logic.common.Authorized;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.vk.getMethods.GetStartScreen;

import java.util.ArrayList;
import java.util.List;

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
            List<IMessage> messages = GetStartScreen.getStartScreen();
            ListView listView = (ListView) findViewById(R.id.list_view_main_messages);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            if (messages != null) {
                for (IMessage message : messages) {
                    adapter.add(message.getText());
                }
            }
            listView.setAdapter(adapter);
            StringBuilder builder = new StringBuilder();
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
