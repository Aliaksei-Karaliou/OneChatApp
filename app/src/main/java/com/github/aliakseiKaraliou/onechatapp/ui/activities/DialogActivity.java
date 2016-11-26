package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.ClearHistoryManager;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.SendManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.managers.VkDialogManager;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogAdapter;

import java.util.List;

public class DialogActivity extends AppCompatActivity {

    IReciever reciever;
    EditText messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        messageTextView = (EditText) findViewById(R.id.dialog_new_message_text);

        Intent intent = getIntent();
        Long peerId = intent.getLongExtra(VkConstants.Extra.PEER_ID, 0);

        if (peerId != 0) {
            reciever = VkReceiverStorage.get(peerId);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(reciever.getName());
        } else {
            reciever = null;
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final VkDialogManager manager = new VkDialogManager();
        manager.startLoading(this, reciever, 0);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_message_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final List<IMessage> result = manager.getResult();
        manager.startLoading(this, reciever, 20);

        final DialogAdapter adapter = new DialogAdapter(result);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                final int itemCount = linearLayoutManager.getItemCount();
                if (lastCompletelyVisibleItemPosition + 1 == itemCount && dy < 0) {
                    final List<IMessage> messageList = manager.getResult();
                    manager.startLoading(DialogActivity.this, reciever, itemCount);
                    if (messageList != null && result != null) {
                        result.addAll(messageList);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
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
                    .setTitle(R.string.confirm_title)
                    .setMessage(R.string.clear_history_confirmation_message)
                    .setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final boolean result = new ClearHistoryManager().clear(reciever);
                            if (!result) {
                                Toast.makeText(DialogActivity.this, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.answer_no, null)
                    .show();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendButtonOnClick(View view) {
        final String message = messageTextView.getText().toString();
        if (!message.equals("")) {
            final boolean success = new SendManager().send(reciever, message);
            if (!success) {
                Toast.makeText(this, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
            } else {
                messageTextView.setText("");
            }
        }
    }
}
