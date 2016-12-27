package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.ClearHistoryManager;
import com.github.aliakseiKaraliou.onechatapp.logic.common.managers.SendManager;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.ChatModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.GroupModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.UserModel;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkAddNewMessageEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkReadAllMessagesEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkDeleteMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogItemCountParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkMessageStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DialogActivity extends AppCompatActivity {

    private static final String OFFSET = "offset";

    private IDialog dialog;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private BroadcastReceiver newEventReceiver;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        final SharedPreferences sharedPreferences = ((App) getApplicationContext()).getApplicationSharedPreferences();
        final boolean theme = sharedPreferences.getBoolean(Constants.Other.DARK_THEME, false);
        setTheme(theme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        final Intent intent = getIntent();
        final long peerId = intent.getLongExtra(Constants.Other.PEER_ID, 0);

        final IReceiver receiver;
        if (peerId != 0) {
            receiver = VkReceiverStorage.get(peerId);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(receiver.getName());
        } else {
            receiver = null;
        }

        dialog = new VkDialog(receiver);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        newEventReceiver = new EventBroadcastReceiver();
        final IntentFilter filter = new IntentFilter(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
        registerReceiver(newEventReceiver, filter);

        final Bundle bundle = new Bundle();
        bundle.putInt(OFFSET, 0);
        getLoaderManager().initLoader(0, bundle, new DialogLoaderCallbacks()).forceLoad();

        progressBar = (ProgressBar) findViewById(R.id.activity_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
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
                            try {
                                new ClearHistoryManager().clear(dialog.getReceiver());
                            }
                            catch (final RuntimeException e){
                                e.printStackTrace();
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
        final String message = messageTextView.getText().toString().trim();
        if (!message.equals("")) {
            try {
                new SendManager().send(dialog.getReceiver(), message);
                messageTextView.setText("");
            }
            catch (final RuntimeException e){
                Toast.makeText(this, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
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
                    if (newMessage.getReceiver().isEquals(dialog.getReceiver())) {
                        dialog.addMessage(0, newMessage);
                    }
                } else if (event instanceof VkDeleteMessageFlagEvent) {
                    final VkDeleteMessageFlagEvent deleteMessageFlagEvent = (VkDeleteMessageFlagEvent) event;
                    final IMessage deleteMessageFlagEventMessage = deleteMessageFlagEvent.getMessage();
                    final VkMessageFlag messageFlag = deleteMessageFlagEvent.getMessageFlag();
                    if (deleteMessageFlagEventMessage != null && messageFlag != null && deleteMessageFlagEventMessage.getReceiver().isEquals(dialog.getReceiver())) {
                        for (final IMessage message : dialog.getAllMessages()) {
                            if (message instanceof VkMessage && deleteMessageFlagEventMessage.isEquals(message)){
                                ((VkMessage) message).deleteFlag(messageFlag);
                                new StringBuilder();
                                break;
                            }
                        }
                    }
                } else if (event instanceof VkReadAllMessagesEvent) {
                    boolean isRead = false;
                    final VkReadAllMessagesEvent allMessagesEvent = (VkReadAllMessagesEvent) event;
                    final IReceiver readReceiver = allMessagesEvent.getReceiver();
                    final IMessage finalMessage = allMessagesEvent.getFinalMessage();
                    if (readReceiver.isEquals(dialog.getReceiver())) {
                        for (final IMessage message : dialog.getAllMessages()) {
                            if (message.isOut()) {
                                if (finalMessage.isEquals(message)) {
                                    isRead = true;
                                }
                                if (isRead  && !message.isRead()) {
                                    ((VkMessage) message).deleteFlag(VkMessageFlag.UNREAD);
                                } else if (isRead && !message.isRead()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class DialogLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<IMessage>> {

        private final Context context = DialogActivity.this;
        private String json;

        @Override
        public Loader<List<IMessage>> onCreateLoader(final int id, final Bundle args) {
            final int offset = args.getInt(OFFSET);
            final String receiverId = Long.toString(dialog.getReceiver().getId());
            return new AsyncTaskLoader<List<IMessage>>(context) {
                @Override
                public List<IMessage> loadInBackground() {
                    List<IMessage> messages = new ArrayList<>();
                    try {
                        final Pair<String, String> peerId = new Pair<>(Constants.Json.PEER_ID, receiverId);
                        if (offset > 0) {
                            final Pair<String, String> offsetPair = new Pair<>(Constants.Params.OFFSET, Integer.toString(offset));
                            json = new VkRequester().doRequest(Constants.Method.MESSAGES_GETHISTORY, peerId, offsetPair);
                        } else {
                            json = new VkRequester().doRequest(Constants.Method.MESSAGES_GETHISTORY, peerId);
                        }

                        final Set<Long> parse = new VkDialogStartParser().parse(json);
                        if (parse != null && parse.size() > 0) {
                            final LongSparseArray<IReceiver> longSparseArray = new VkReceiverDataParser().parse(parse);
                            VkReceiverStorage.putAll(longSparseArray);
                            final ORM recieverORM = ((App) context.getApplicationContext()).getRecieverORM();

                            final List<IUser> userList = new ArrayList<>();
                            final List<IChat> chatList = new ArrayList<>();
                            final List<IGroup> groupList = new ArrayList<>();

                            if (longSparseArray != null) {

                                for (int i = 0; i < longSparseArray.size(); i++) {
                                    final IReceiver receiver = longSparseArray.valueAt(i);
                                    if (receiver instanceof IUser) {
                                        userList.add((IUser) receiver);
                                    } else if (receiver instanceof IChat) {
                                        chatList.add((IChat) receiver);
                                    } else if (receiver instanceof IGroup) {
                                        groupList.add((IGroup) receiver);
                                    }
                                }
                            }

                            recieverORM.insertAll(Constants.Db.USERS, UserModel.convertTo(userList));
                            recieverORM.insertAll(Constants.Db.CHATS, ChatModel.convertTo(chatList));
                            recieverORM.insertAll(Constants.Db.GROUPS, GroupModel.convertTo(groupList));
                        }
                        messages = new VkDialogFinalParser().parse(context, json);

                    } catch (final IOException e) {
                        e.printStackTrace();
                    }

                    return messages;
                }
            };
        }


        @Override
        public void onLoadFinished(final Loader<List<IMessage>> loader, final List<IMessage> data) {
            final int messageCount = dialog.getAllMessages().size();
            if (messageCount == 0) {
                dialog.addMessages(data);
                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_message_recycler_view);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                layoutManager.setReverseLayout(true);
                recyclerView.setLayoutManager(layoutManager);

                progressBar.setVisibility(View.INVISIBLE);

                if (json != null) {
                    final Integer parse = new VkDialogItemCountParser().parse(json);
                    if (parse != null) {
                        dialog.setMessageCount(parse);
                        new StringBuilder();
                    }
                }

                adapter = new DialogAdapter(context, dialog);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        final int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        final int totalItem = layoutManager.getItemCount();

                        if (lastVisibleItem + 1 == totalItem && dy < 0) {

                            final Bundle bundle = new Bundle();
                            bundle.putInt(OFFSET, totalItem);
                            getLoaderManager().restartLoader(0, bundle, new DialogLoaderCallbacks()).forceLoad();
                        }
                    }
                });
            } else {
                dialog.addMessages(data);
                adapter.notifyItemRangeInserted(messageCount, data.size());
            }

            VkMessageStorage.putAll(data);
        }

        @Override
        public void onLoaderReset(final Loader<List<IMessage>> loader) {
            Toast.makeText(context, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
        }
    }
}
