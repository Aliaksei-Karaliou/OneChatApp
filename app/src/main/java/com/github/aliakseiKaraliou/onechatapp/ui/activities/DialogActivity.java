package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.ClearHistoryManager;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.SendManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkAddNewMessageEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkDeleteMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.managers.VkDialogManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkMessageStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogAdapter;

import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends AppCompatActivity {

    private IReceiver receiver;
    private List<IMessage> messageList = new ArrayList<>();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    BroadcastReceiver newEventReceiver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        final Intent intent = getIntent();
        final long peerId = intent.getLongExtra(Constants.Other.PEER_ID, 0);

        if (peerId != 0) {
            receiver = VkReceiverStorage.get(peerId);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(receiver.getName());
        } else {
            receiver = null;
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newEventReceiver = new EventBroadcastReceiver();
        final IntentFilter filter = new IntentFilter(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
        registerReceiver(newEventReceiver, filter);

        final VkDialogManager manager = new VkDialogManager();
        manager.startLoading(this, receiver, 0);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_message_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageList = manager.getResult();
        VkMessageStorage.putAll(messageList);
        manager.startLoading(this, receiver, 20);

        adapter = new DialogAdapter(this, messageList);
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                try {
                    super.onScrolled(recyclerView, dx, dy);
                    final int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    final int itemCount = linearLayoutManager.getItemCount();
                    if (lastCompletelyVisibleItemPosition + 1 == itemCount && dy < 0) {

                        final List<IMessage> newMessageList = manager.getResult();

                        if (messageList != null && newMessageList != null) {
                            messageList.addAll(newMessageList);
                            adapter.notifyDataSetChanged();
                            VkMessageStorage.putAll(newMessageList);
                            manager.startLoading(DialogActivity.this, receiver, messageList.size());
                        }


                    }
                }
                catch (final Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dialog_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.user_menu_item_clear_history) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirm_title)
                    .setMessage(R.string.clear_history_confirmation_message)
                    .setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            final boolean result = new ClearHistoryManager().clear(receiver);
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

    public void sendButtonOnClick(final View view) {
        final EditText messageTextView = (EditText) findViewById(R.id.dialog_new_message_text);
        final String message = messageTextView.getText().toString();
        if (!message.equals("")) {
            final boolean success = new SendManager().send(receiver, message);
            if (!success) {
                Toast.makeText(this, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
            } else {
                messageTextView.setText("");
            }
        }
    }

    public class NewMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ArrayList<Parcelable> parcelableArrayListExtra = intent.getParcelableArrayListExtra(Constants.Params.MESSAGE);
            final List<IMessage> newMessages = new ArrayList<>();
            if (parcelableArrayListExtra != null) {
                for (final Parcelable parcelable : parcelableArrayListExtra) {
                    if (parcelable instanceof IMessage) {
                        newMessages.add((IMessage) parcelable);
                    }
                }
                if (newMessages.size() > 0) {
                    for (final IMessage message : newMessages) {
                        if (message.getReceiver().isEquals(receiver)) {
                            messageList.add(0, message);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(newEventReceiver);
    }

    private class EventBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final List<IEvent> eventList = intent.getParcelableArrayListExtra(Constants.Other.EVENT_LIST);
            for (final IEvent event : eventList) {
                if (event instanceof VkAddNewMessageEvent) {
                    final IMessage newMessage = ((VkAddNewMessageEvent) event).getMessage();
                    if (newMessage.getReceiver().isEquals(receiver)) {
                        messageList.add(0, newMessage);
                        adapter.notifyDataSetChanged();
                    }
                } else if (event instanceof VkDeleteMessageFlagEvent) {
                    final VkDeleteMessageFlagEvent deleteMessageFlagEvent = (VkDeleteMessageFlagEvent) event;
                    final IMessage deleteMessageFlagEventMessage = deleteMessageFlagEvent.getMessage();
                    final VkMessageFlag messageFlag = deleteMessageFlagEvent.getMessageFlag();
                    if (deleteMessageFlagEventMessage != null && messageFlag != null && deleteMessageFlagEventMessage.getReceiver().isEquals(receiver)) {
                        for (final IMessage message : messageList) {
                            if (message instanceof VkMessage && deleteMessageFlagEventMessage.isEquals(message)){
                                ((VkMessage) message).deleteFlag(messageFlag);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
