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
import com.alonso.myuniapplication.business.SingleChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChatActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef, singleChatReef;
    private ValueEventListener mSingleChatListener;

    private String singleChatKey, messageReceiverName, getMessageReceiverImage;

    private TextView guestUserNameTV;
    private CircleImageView guestUserProfileImage;

    private Toolbar chatToolBar;

    private ImageButton sendMessageButton;
    private EditText messageInputText;

    private SingleChat currentSingleChat;

    private List<ChatMessage> chatMessages = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        singleChatKey = getIntent().getExtras().get("chat_id").toString();
        singleChatKey = "-LbU4U_YmIuUn2x8Dzjf";
        messageReceiverName = getIntent().getExtras().get("visit_user_id").toString();
        getMessageReceiverImage = getIntent().getExtras().get("visit_user_profile_image").toString();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        singleChatReef = FirebaseDatabase.getInstance().getReference().child("SingleChats").child(singleChatKey);

        InitializeFields();

        guestUserNameTV.setText(messageReceiverName);
        if(!getMessageReceiverImage.equals(""))
            Picasso.get().load(getMessageReceiverImage).placeholder(R.drawable.profile_image).into(guestUserProfileImage);

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

        /*ValueEventListener singleChatListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("SingleChatActivity", "Get current single chat done");
                currentSingleChat = dataSnapshot.getValue(SingleChat.class);
                chatMessages=currentSingleChat.getMessages();
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SingleChatActivity", "Error getting Data: " + databaseError.getMessage());
            }
        };
        singleChatReef.addValueEventListener(singleChatListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mSingleChatListener = singleChatListener;*/

        rootRef.child("SingleChats").child(singleChatKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("SingleChatActivity", "Get current single chat done");
                        currentSingleChat = dataSnapshot.getValue(SingleChat.class);
                        bindMessages(chatMessages, currentSingleChat.getMessages());
                        messageAdapter.notifyDataSetChanged();
                        userMessagesRecyclerView.smoothScrollToPosition(userMessagesRecyclerView.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        Log.i("SingleChatActivity", "Amount of chats: " + messageAdapter.getItemCount());
    }

    private void bindMessages(List<ChatMessage> messagesToLoad, List<ChatMessage> messagesForLoad) {
        messagesToLoad.clear();
        for(ChatMessage chatMessage: messagesForLoad){
            messagesToLoad.add(chatMessage);
        }
    }

    private void InitializeFields() {
        chatToolBar = (Toolbar) findViewById(R.id.single_chat_toolbar);
        this.setSupportActionBar(chatToolBar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custome_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        //Va debajo de el seteo de actionBarView
        guestUserNameTV = (TextView) findViewById(R.id.chat_name);
        guestUserProfileImage = (CircleImageView) findViewById(R.id.profile_chat_image);
        sendMessageButton = (ImageButton) findViewById(R.id.single_chat_send_message_button);
        messageInputText = (EditText) findViewById(R.id.input_single_chat_message);

        messageAdapter = new MessageAdapter(chatMessages);

        /*runOnUiThread(new Runnable() {

            @Override
            public void run() {
                userMessagesRecyclerView = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
                linearLayoutManager = new LinearLayoutManager(SingleChatActivity.this);
                userMessagesRecyclerView.setLayoutManager(linearLayoutManager);
                userMessagesRecyclerView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
            }
        });*/
        userMessagesRecyclerView = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesRecyclerView.setLayoutManager(linearLayoutManager);
        userMessagesRecyclerView.setAdapter(messageAdapter);
    }



    private void sendMessage(){
        String messageTest = messageInputText.getText().toString();
        if(TextUtils.isEmpty(messageTest)){
            Toast.makeText(SingleChatActivity.this, "Por favor, escribe un mensaje primero...", Toast.LENGTH_SHORT).show();
        } else {
            ChatMessage chatMessage = createChatMessage(messageInputText.getText().toString());
            currentSingleChat.addMessage(chatMessage);

            messageInputText.setText("");
            singleChatReef.setValue(currentSingleChat).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("SingleChatActivity", "Error saving message", e);
                }
            });
            /*rootRef.child("SingleChats").child("-LbU4U_YmIuUn2x8Dzjf").setValue(currentSingleChat)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SingleChatActivity", "Error saving message", e);
                        }
                    });*/
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
}
