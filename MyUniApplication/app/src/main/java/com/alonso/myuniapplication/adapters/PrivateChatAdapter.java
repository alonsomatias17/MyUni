package com.alonso.myuniapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alonso.myuniapplication.Chat.ChatActivity;
import com.alonso.myuniapplication.Chat.SingleChatActivity;
import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.SingleChat;
import com.alonso.myuniapplication.business.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.MyViewHolder>{

    private List<SingleChat> singleChats;
    Context context;
    private String currentUser;


    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public PrivateChatAdapter(Context context, List<SingleChat> singleChats) {
        this.singleChats = singleChats;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CircleImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.users_profile_name);
            profileImage =  view.findViewById(R.id.users_profile_image);
        }
    }

    @Override
    public PrivateChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
        return new PrivateChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return singleChats.size();
    }

    @Override
    //Itera al setear la lista por cada uno de los elementos
    public void onBindViewHolder(final PrivateChatAdapter.MyViewHolder holder, int position) {
        final int pos = position;
        final UserDTO userDTO = getGustUserDTO(position);
        holder.textView.setText(userDTO.getUserName());
        if(!userDTO.getProfileImage().equals(""))
            Picasso.get().load(userDTO.getProfileImage()).into(holder.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(context, SingleChatActivity.class);
                //TODO: pasar ID de chat
                chatIntent.putExtra("chat_id", userDTO.getEmail());
                chatIntent.putExtra("visit_user_id", userDTO.getUserName());
                chatIntent.putExtra("visit_user_profile_image", userDTO.getProfileImage());
                context.startActivity(chatIntent);

            }
        });
        // perform on Click Event Listener on CheckedTextView
//        setSubjectOnClickListener(holder, pos);
    }

    private UserDTO getGustUserDTO(int position) {
        if (singleChats.isEmpty())
            return new UserDTO();

        if(singleChats.get(position).getParticipants().get(0).getEmail().equals(this.currentUser))
            return singleChats.get(position).getParticipants().get(1);
        else
            return singleChats.get(position).getParticipants().get(0);
    }

    /*private void setSubjectOnClickListener(final MyViewHolder holder, final int pos) {
        holder.subjectCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.subjectCheckedTextView.isChecked();
                if (value) {
                    Subject subject = singleChats.get(pos);
                    subject.setState(Subject.NOT_APPROVED);
                    // set check mark drawable and set checked property to false
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check_ic);
                    holder.subjectCheckedTextView.setChecked(false);
                    Toast.makeText(context, "un-Checked", Toast.LENGTH_SHORT).show();
                } else {
                    Subject subject = singleChats.get(pos);
                    subject.setState(Subject.ON_GOING);
                    // set check mark drawable and set checked property to true
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.ic_create_black_24dp);
                    holder.subjectCheckedTextView.setChecked(true);
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}
