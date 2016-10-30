package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.vk.adapters.VkDialogAdapter;
import com.example.korol.onechatapp.logic.vk.entities.VkSenderType;
import com.example.korol.onechatapp.logic.vk.getMethods.VkGetDialog;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

public class ConversationActivity extends AppCompatActivity {

    private VkSenderType type;
    private ISender interlocutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        final Intent intent = getIntent();
        long interlocutorId = intent.getLongExtra("ID", -1);
        type = VkSenderType.valueOf(intent.getStringExtra("TYPE"));
        Toast.makeText(this, Long.toString(interlocutorId), Toast.LENGTH_SHORT).show();
       /* if (type == VkSenderType.USER) {
            interlocutor = VkIdToUserStorage.getUser(interlocutorId);
        } else
            Toast.makeText(this, "In development", Toast.LENGTH_SHORT).show();

        if (interlocutor != null) {
            IDialog list = VkGetDialog.getDialog(interlocutor);
            VkDialogAdapter adapter = new VkDialogAdapter(this, list);
            ((ListView) findViewById(R.id.activity_conversation_message_list)).setAdapter(adapter);
        }*/


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
