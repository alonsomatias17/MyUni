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
import com.alonso.myuniapplication.business.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    DocumentReference userRef;
    private FirebaseFirestore firebaseFirestore;


    private List<ChatMessage> userMessages;
    private DatabaseReference singleChatReef;
    private FirebaseUser firebaseUser;
    private Map<String, String> usersProfilesPic = new HashMap<>();


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
        final MessageViewHolder holder1 = holder;
        final String userSenderEmail = chatMessage.getSenderEmail();
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


        if(userSenderEmail.equals(firebaseUser.getEmail())){
            holder.senderMessageText.setVisibility(View.VISIBLE);
            holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.senderMessageText.setTextColor(Color.BLACK);
            holder.senderMessageText.setText(message);

        } else {
            if (usersProfilesPic.get(userSenderEmail) != null ) {
                Picasso.get()
                        .load(usersProfilesPic.get(userSenderEmail))
                        .placeholder(R.drawable.profile_image).into(holder.receiverProfileImage);
            } else {
                firebaseFirestore = FirebaseFirestore.getInstance();
                userRef = firebaseFirestore.collection("users").document(userSenderEmail);
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if(!user.getProfileImageUri().equals("")) {
                            Picasso.get().load(user.getProfileImageUri())
                                    .placeholder(R.drawable.profile_image).into(holder1.receiverProfileImage);
                        }
                        usersProfilesPic.put(userSenderEmail, user.getProfileImageUri());
                    }
                });
            }

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
