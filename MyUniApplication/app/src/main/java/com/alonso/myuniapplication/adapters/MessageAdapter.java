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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<ChatMessage> userMessages;
    private DatabaseReference singleChatReef;
    private FirebaseUser firebaseUser;

    public MessageAdapter( List<ChatMessage> userMessages){
        this.userMessages = userMessages;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
        }

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout, parent, false);

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
        holder.receiverProfileImage.setVisibility(View.INVISIBLE);
        holder.senderMessageText.setVisibility(View.INVISIBLE);

        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/myuni-aade3.appspot.com/o/Profile%20Images%2Fmatias.alonso%40myuni.com.jpg?alt=media&token=f0726978-8076-463e-a3c5-cf701531621f")
                .placeholder(R.drawable.profile_image).into(holder.receiverProfileImage);


        if(userSenderEmail.equals(firebaseUser.getEmail())){
            holder.senderMessageText.setVisibility(View.VISIBLE);
            holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.senderMessageText.setTextColor(Color.BLACK);
            holder.senderMessageText.setText(message);

        } else {
            holder.receiverMessageText.setVisibility(View.VISIBLE);
            holder.receiverProfileImage.setVisibility(View.VISIBLE);

            holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.receiverMessageText.setTextColor(Color.BLACK);
            holder.receiverMessageText.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return userMessages.size();
    }

}
