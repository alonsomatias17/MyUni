package com.alonso.myuniapplication.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.MessageViewHolder> {

    private List<ChatMessage> userMessages;
    private DatabaseReference singleChatReef;
    private FirebaseUser firebaseUser;

    public GroupMessageAdapter(List<ChatMessage> userMessages){
        this.userMessages = userMessages;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, receiverMessageText, senderMessageName;
//        public CircleImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageName = (TextView) itemView.findViewById(R.id.senderName);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
        }

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_group_messages_layout, parent, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage chatMessage = userMessages.get(position);
        String userSenderEmail = chatMessage.getSenderEmail();
        String message = chatMessage.getMessage();

       /* singleChatReef = FirebaseDatabase.getInstance().getReference().child("SingleChats").child("chatID");

        singleChatReef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        holder.receiverMessageText.setVisibility(View.INVISIBLE);
        holder.senderMessageName.setVisibility(View.INVISIBLE);
        holder.senderMessageText.setVisibility(View.INVISIBLE);


        if(userSenderEmail.equals(firebaseUser.getEmail())){
            holder.senderMessageText.setVisibility(View.VISIBLE);
            holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.senderMessageText.setTextColor(Color.BLACK);
            holder.senderMessageText.setText(message);



        } else {
            holder.receiverMessageText.setVisibility(View.VISIBLE);
            holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.receiverMessageText.setTextColor(Color.BLACK);
            holder.receiverMessageText.setText(message);
            //TODO: add kvs color tu sender name
            holder.senderMessageName.setVisibility(View.VISIBLE);
            holder.senderMessageName.setTextColor(Color.CYAN);
            holder.senderMessageName.setText(userSenderEmail + ":");
        }
    }

    @Override
    public int getItemCount() {
        return userMessages.size();
    }

}
