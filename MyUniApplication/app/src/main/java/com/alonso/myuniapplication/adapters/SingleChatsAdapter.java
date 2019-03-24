package com.alonso.myuniapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChatsAdapter extends RecyclerView.Adapter<SingleChatsAdapter.ChatsViewHolder>{

    private List<UserDTO> userDTOS;
    Context context;

    public SingleChatsAdapter(Context context, List<UserDTO> userDTOS) {
        this.userDTOS = userDTOS;
        this.context = context;
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView userName;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

//            profileImage = itemView.findViewById(R.id.users_profile_image);
            userName = itemView.findViewById(R.id.users_profile_name);
        }
    }


    @NonNull
    @Override
    public SingleChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
        return new SingleChatsAdapter.ChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleChatsAdapter.ChatsViewHolder holder, int position) {
        UserDTO userDTO = userDTOS.get(position);
        holder.userName.setText(userDTO.getUserName());
//        if(!userDTO.getProfileImage().equals(""))
//            Picasso.get().load(userDTO.getProfileImage()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
