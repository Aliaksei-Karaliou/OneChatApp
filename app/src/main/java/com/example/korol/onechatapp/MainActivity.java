package com.example.korol.onechatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.korol.onechatapp.logic.common.Authorized;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ImageFromUrl;
import com.example.korol.onechatapp.logic.vk.getMethods.GetStartScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String ATTRIBUTE_USER_NAME = "Name";
    private static final String ATTRIBUTE_USER_TEXT = "Message";
    private static final String ATTRIBUTE_USER_PHOTO = "Photo";


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
            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            Map<String, Object> map;
            IMessage messageException;
            try {
                for (IMessage message : messages) {
                    //   messageException = message;
                    map = new HashMap<>();
                    if (message.getSender() != null) {
                        Bitmap d = ImageFromUrl.getImageFromUrl(message.getSender().getPhotoUrl());
                        map.put(ATTRIBUTE_USER_PHOTO, ImageFromUrl.getImageFromUrl(message.getSender().getPhotoUrl()));
                        map.put(ATTRIBUTE_USER_NAME, message.getSender().getName());
                    } else {
                        map.put(ATTRIBUTE_USER_PHOTO, ImageFromUrl.getImageFromUrl(message.getSender().getPhotoUrl()));
                        map.put(ATTRIBUTE_USER_NAME, "");
                    }
                    map.put(ATTRIBUTE_USER_TEXT, message.getText());

                    data.add(map);
                }

            } catch (Exception e) {
                StringBuilder builder = new StringBuilder();
            }
            String[] from = new String[]{ATTRIBUTE_USER_NAME, ATTRIBUTE_USER_TEXT, ATTRIBUTE_USER_PHOTO};
            int[] to = {R.id.start_screen_message_name, R.id.start_screen_message_message, R.id.start_screen_message_avatar};

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.start_screen_message_item, from, to);
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
