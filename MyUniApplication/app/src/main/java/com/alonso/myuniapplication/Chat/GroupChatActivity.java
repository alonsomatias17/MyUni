package com.alonso.myuniapplication.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.adapters.MessageAdapter;
import com.alonso.myuniapplication.business.ChatMessage;
import com.alonso.myuniapplication.business.GroupChat;
import com.alonso.myuniapplication.business.SingleChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef, groupsChatReef;

    private String groupChatKey;

    private TextView groupNameTV;

    private Toolbar chatToolBar;

    private ImageButton sendMessageButton;
    private EditText messageInputText;

    private GroupChat currentGroupChat;

    private List<ChatMessage> chatMessages = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        groupChatKey = getIntent().getExtras().get("chat_id").toString();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        groupsChatReef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupChatKey);

        initializeFields();

        groupNameTV.setText(groupChatKey);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        rootRef.child("Groups").child(groupChatKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("SingleChatActivity", "Get current single chat done");
                        currentGroupChat = dataSnapshot.getValue(GroupChat.class);
                        bindMessages(chatMessages, currentGroupChat.getMessages());
                        messageAdapter.notifyDataSetChanged();
                        userMessagesRecyclerView.smoothScrollToPosition(userMessagesRecyclerView.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        Log.i("SingleChatActivity", "Amount of chats: " + messageAdapter.getItemCount());
    }

    private void initializeFields() {
        chatToolBar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        this.setSupportActionBar(chatToolBar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custome_group_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        //Va debajo de el seteo de actionBarView
        groupNameTV = (TextView) findViewById(R.id.group_chat_name);
        sendMessageButton = (ImageButton) findViewById(R.id.group_chat_send_message_button);
        messageInputText = (EditText) findViewById(R.id.input_group_chat_message);

        messageAdapter = new MessageAdapter(chatMessages);

        userMessagesRecyclerView = (RecyclerView) findViewById(R.id.group_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesRecyclerView.setLayoutManager(linearLayoutManager);
        userMessagesRecyclerView.setAdapter(messageAdapter);
    }

    private void sendMessage(){
        String messageTest = messageInputText.getText().toString();
        if(TextUtils.isEmpty(messageTest)){
            Toast.makeText(GroupChatActivity.this, "Por favor, primero escribe un mensaje...", Toast.LENGTH_SHORT).show();
        } else {
            ChatMessage chatMessage = createChatMessage(messageInputText.getText().toString());
            currentGroupChat.addMessage(chatMessage);

            messageInputText.setText("");
            groupsChatReef.setValue(currentGroupChat).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("GroupChatActivity", "Error saving message", e);
                }
            });
        }
    }

    private ChatMessage createChatMessage(String message) {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());

        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm:ss");
        String currentTime = currentTimeFormat.format(calForDate.getTime());

        return new ChatMessage(firebaseUser.getEmail(), currentDate, message, firebaseUser.getDisplayName(), currentTime);
    }

    private void bindMessages(List<ChatMessage> messagesToLoad, List<ChatMessage> messagesForLoad) {
        messagesToLoad.clear();
        for(ChatMessage chatMessage: messagesForLoad){
            messagesToLoad.add(chatMessage);
        }
    }
}
