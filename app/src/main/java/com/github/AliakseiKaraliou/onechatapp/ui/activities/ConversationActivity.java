package com.github.AliakseiKaraliou.onechatapp.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.AliakseiKaraliou.onechatapp.R;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.AliakseiKaraliou.onechatapp.ui.adapters.DialogRecyclerAdapter;

public class ConversationActivity extends AppCompatActivity {

    private IReciever reciever;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        final Intent intent = getIntent();
        reciever = intent.getParcelableExtra("Reciever");

        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(reciever.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.messageEditText = (EditText) findViewById(R.id.activity_conversation_new_message_text);
        this.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (messageEditText.getText().length() > 0)
                    findViewById(R.id.activity_conversation_send_button).setEnabled(true);
                else
                    findViewById(R.id.activity_conversation_send_button).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final IDialog dialog = null;
        DialogRecyclerAdapter adapter = new DialogRecyclerAdapter(dialog);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_conversation_message_recycler_view);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int totalItemCount = layoutManager.getItemCount();
                final int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition + 1 == totalItemCount && dy < 0) {
                    //final List<IMessage> messageList = VkGetDialog.getMessageList(ConversationActivity.this, reciever, dialog.getMessages().size());
                    // dialog.add(new ArrayList<IMessage>());
                    recyclerView.getAdapter().notifyDataSetChanged();
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
                            /*final String peer_id = new VkRequester("messages.deleteDialog", new Pair<>("peer_id", Long.toString(reciever.getId()))).execute(null);
                            if (!"{\"response\":1}".equals(peer_id))
                                Toast.makeText(ConversationActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();*/
                        }
                    })
                    .setNegativeButton(R.string.answer_no, null)
                    .show();
            return true;
        }
        else if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendButtonOnClick(View view) {
        final String message = messageEditText.getText().toString();
        /*if (MessageSender.getInstance(reciever.getSocialNetwork(), reciever.getReceiverType()).send(reciever, message))
            messageEditText.setText("");*/
    }
}
