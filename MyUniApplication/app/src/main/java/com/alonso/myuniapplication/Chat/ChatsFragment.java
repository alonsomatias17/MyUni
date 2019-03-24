package com.alonso.myuniapplication.Chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.adapters.PrivateChatAdapter;
import com.alonso.myuniapplication.adapters.SingleChatsAdapter;
import com.alonso.myuniapplication.business.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View privateChatsView;
    private RecyclerView chatsRecyclerView;

    private DatabaseReference chatsRef;
//    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference chatsStorageRef;

    private FirebaseAuth mAuth;

    SingleChatsAdapter singleChatsAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         privateChatsView = inflater.inflate(R.layout.fragment_chats, container, false);

         mAuth = FirebaseAuth.getInstance();
//         chatsStorageRef = FirebaseStorage.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getEmail());
//         chatsRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getEmail());

        chatsRecyclerView = (RecyclerView) privateChatsView.findViewById(R.id.single_chat_list);
        chatsRecyclerView.setLayoutManager( new LinearLayoutManager(this.getActivity()));
        chatsRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));

        PrivateChatAdapter privateChatAdapter = new PrivateChatAdapter(this.getActivity(), chatsMock());

//        singleChatsAdapter = new SingleChatsAdapter(this.getActivity(), chatsMock() );
        chatsRecyclerView.setAdapter(privateChatAdapter);

        privateChatAdapter.notifyDataSetChanged();

        return privateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();

       /* //TODO: cambiar a una clase que use realmente
        FirebaseRecyclerOptions<User>  options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(chatsStorageRef, User.class)
                .build();

        FirebaseRecyclerAdapter<User, ChatsViewHolder> adapter
                = new FirebaseRecyclerAdapter<User, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull User model) {

            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                return new ChatsViewHolder(view);
            }
        };
        chatsRecyclerView.setAdapter(adapter);
        adapter.startListening();

        */

    }

    /*public static class ChatsViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView userName;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.users_profile_image);
            userName = itemView.findViewById(R.id.users_profile_image);
        }
    }*/

    private List<UserDTO> chatsMock(){
        List<UserDTO> userDTOS = new ArrayList<>();
        userDTOS.add(new UserDTO("Julian Rodriguez", "julian@myuni.com", "https://firebasestorage.googleapis.com/v0/b/myuni-aade3.appspot.com/o/Profile%20Images%2Fmatias.alonso%40myuni.com.jpg?alt=media&token=f0726978-8076-463e-a3c5-cf701531621f"));
        userDTOS.add(new UserDTO("Pablo Escobar", "pablo@myuni.com", "https://firebasestorage.googleapis.com/v0/b/myuni-aade3.appspot.com/o/Profile%20Images%2Fmatias.alonso%40myuni.com.jpg?alt=media&token=f0726978-8076-463e-a3c5-cf701531621f"));
        return userDTOS;
    }
}
