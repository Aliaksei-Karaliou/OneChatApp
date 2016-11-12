package com.example.korol.onechatapp.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.common.MessageSender;
import com.example.korol.onechatapp.logic.vk.VkRequester;
import com.example.korol.onechatapp.logic.vk.getMethods.VkGetDialog;
import com.example.korol.onechatapp.ui.adapters.DialogRecyclerAdapter;

import java.util.concurrent.ExecutionException;

public class ConversationActivity extends AppCompatActivity {

    ISender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        final Intent intent = getIntent();
        sender = intent.getParcelableExtra("Sender");

        getSupportActionBar().setTitle(sender.getName());

        IDialog dialog = VkGetDialog.getDialog(this, sender);
        DialogRecyclerAdapter adapter = new DialogRecyclerAdapter(dialog);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_conversation_message_recycler_view);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_menu_item_clear_history) {
            new AlertDialog.Builder(this)
                    .setTitle("Please confirm")
                    .setMessage("Are you sure you want to delete all mesages in this chat? Thi action can't be undone.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                final String peer_id = new VkRequester("messages.deleteDialog", new Pair<String, String>("peer_id", Long.toString(sender.getId()))).execute(null);
                                if (!"{\"response\":1}".equals(peer_id))
                                    Toast.makeText(ConversationActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendButtonOnClick(View view) {
        final EditText messageEditText = (EditText) findViewById(R.id.activity_conversation_new_message_text);
        final String message = messageEditText.getText().toString();
        if (MessageSender.getInstance(sender.getSocialNetwork(), sender.getSenderType()).send(sender, message))
            messageEditText.setText("");
    }
}
