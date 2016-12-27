package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkAddNewMessageEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags.VkDeleteMessageFlagEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListFinalParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkDialogsListStartParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkReceiverDataParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkMessageStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.services.LongPollService;
import com.github.aliakseiKaraliou.onechatapp.ui.adapters.DialogListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DialogsListActivity extends AppCompatActivity {

    private final String OFFSET = "offset";



    private List<IMessage> messages;
    private BroadcastReceiver broadcastReceiver;
    private SharedPreferences sharedPreferences;
    private DialogListAdapter adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        sharedPreferences = ((App) getApplicationContext()).getApplicationSharedPreferences();
        final boolean theme = sharedPreferences.getBoolean(Constants.Other.DARK_THEME, false);
        setTheme(theme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs_list);

        if (VkInfo.isUserAuthorized()) {
            broadcastReceiver = new NewEventBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
            registerReceiver(broadcastReceiver, intentFilter);

            final Bundle bundle = new Bundle();
            bundle.putInt(OFFSET, 0);
            getLoaderManager().initLoader(0, bundle, new DialogListLoaderCallback()).forceLoad();

            startService(new Intent(this, LongPollService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VkInfo.userSetAuth(sharedPreferences);
        if (VkInfo.isUserAuthorized() && messages == null) {
            progressBar = (ProgressBar) findViewById(R.id.activity_progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            final Bundle bundle = new Bundle();
            bundle.putInt(OFFSET, 0);
            getLoaderManager().initLoader(0, bundle, new DialogListLoaderCallback()).forceLoad();

            startService(new Intent(this, LongPollService.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dialog_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.dialog_list_item_authorization) {
            startActivity(new Intent(this, AuthActivity.class));
        } else if (item.getItemId() == R.id.dialog_list_preferences) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private class NewEventBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final List<IEvent> eventList = intent.getParcelableArrayListExtra(Constants.Other.EVENT_LIST);
            for (final IEvent event : eventList) {
                if (event instanceof VkAddNewMessageEvent) {
                    final IMessage currentBroadcastMessage = ((VkAddNewMessageEvent) event).getMessage();
                    final IReceiver receiver = currentBroadcastMessage.getReceiver();
                    for (int i = 0; i < messages.size(); i++) {
                        final IMessage message = messages.get(i);
                        if (receiver.isEquals(message.getReceiver())) {
                            messages.remove(i);
                            break;
                        }
                    }
                    messages.add(0, currentBroadcastMessage);
                }
                else if (event instanceof VkDeleteMessageFlagEvent){
                    final VkDeleteMessageFlagEvent deleteMessageFlagEvent = (VkDeleteMessageFlagEvent) event;
                    final IMessage deleteMessageFlagEventMessage = deleteMessageFlagEvent.getMessage();
                    final VkMessageFlag messageFlag = deleteMessageFlagEvent.getMessageFlag();
                    for (final IMessage message : messages) {
                        if (message instanceof VkMessage && message.isEquals(deleteMessageFlagEventMessage)){
                            final VkMessage vkMessage = (VkMessage) message;
                            vkMessage.deleteFlag(messageFlag);
                            break;
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class DialogListLoaderCallback implements LoaderManager.LoaderCallbacks<List<IMessage>> {

        final Context context = DialogsListActivity.this;

        @Override
        public Loader<List<IMessage>> onCreateLoader(final int id, final Bundle args) {
            final int offset = args.getInt(OFFSET);
            return new AsyncTaskLoader<List<IMessage>>(DialogsListActivity.this) {
                @Override
                public List<IMessage> loadInBackground() {
                    final List<IMessage> messageList = new ArrayList<>();
                    try {
                        final String jsonString;
                        if (offset > 0) {
                            final Pair<String, String> offsetPair = new Pair<>(Constants.Params.OFFSET, Integer.toString(offset));
                            jsonString = new VkRequester().doRequest(Constants.Method.MESSAGES_GETDIALOGS, offsetPair);
                        } else {
                            jsonString = new VkRequester().doRequest(Constants.Method.MESSAGES_GETDIALOGS);
                        }
                        final Set<Long> idParse = new VkDialogsListStartParser().parse(jsonString);
                        if (idParse != null && idParse.size() > 0) {
                            final LongSparseArray<IReceiver> parse = new VkReceiverDataParser().parse(idParse);
                            VkReceiverStorage.putAll(parse);
                        }

                        return new VkDialogsListFinalParser().parse(context, jsonString);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    return messageList;
                }
            };
        }


        @Override
        public void onLoadFinished(final Loader<List<IMessage>> loader, final List<IMessage> messageList) {
            if (messages == null) {
                messages = messageList;
                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_dialogs_list_messages);

                final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

                progressBar.setVisibility(View.INVISIBLE);

                adapter = new DialogListAdapter(context, messageList);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        final int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        final int totalItem = layoutManager.getItemCount();

                        if (lastVisibleItem + 1 == totalItem && dy > 0) {

                            final Bundle bundle = new Bundle();
                            bundle.putInt(OFFSET, totalItem);
                            getLoaderManager().restartLoader(0, bundle, new DialogListLoaderCallback()).forceLoad();
                        }
                    }
                });

                adapter.onItemClick(new DialogListAdapter.OnMessageClick() {
                    @Override
                    public void onClick(final long peerId) {
                        final Intent intent = new Intent(DialogsListActivity.this, DialogActivity.class);
                        intent.putExtra(Constants.Other.PEER_ID, peerId);
                        startActivity(intent);
                    }
                });

            } else {
                final int size = messages.size();
                messages.addAll(messageList);
                adapter.notifyItemRangeInserted(size, messages.size() - size);
            }
            VkMessageStorage.putAll(messageList);

        }

        @Override
        public void onLoaderReset(final Loader<List<IMessage>> loader) {

        }
    }
}
