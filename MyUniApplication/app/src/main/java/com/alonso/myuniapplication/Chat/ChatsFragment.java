package com.alonso.myuniapplication.Chat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.adapters.PrivateChatAdapter;
import com.alonso.myuniapplication.adapters.SingleChatsAdapter;
import com.alonso.myuniapplication.business.SingleChat;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


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

    FirebaseFirestore db;
    DocumentReference userRef;


    private DatabaseReference singleChatsRef;
    private ArrayList<SingleChat> chats = new ArrayList<>();
    PrivateChatAdapter privateChatAdapter;
    private User currentUser = new User("");
    private FirebaseUser firebaseUser;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        privateChatsView = inflater.inflate(R.layout.fragment_chats, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUserFS(firebaseUser.getEmail());
//         chatsStorageRef = FirebaseStorage.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getEmail());
//         chatsRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getEmail());


        chatsRecyclerView = (RecyclerView) privateChatsView.findViewById(R.id.single_chat_list);
        chatsRecyclerView.setLayoutManager( new LinearLayoutManager(this.getActivity()));
        chatsRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));

        privateChatAdapter = new PrivateChatAdapter(this.getActivity(), chats);

//        singleChatsAdapter = new SingleChatsAdapter(this.getActivity(), chatsMock() );
        chatsRecyclerView.setAdapter(privateChatAdapter);

        privateChatAdapter.notifyDataSetChanged();

        singleChatsRef = FirebaseDatabase.getInstance().getReference().child("SingleChats");

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


    private void retrieveAndDisplayGroups() {
        singleChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, SingleChat> singleChatWithKeys = new HashMap<>();
                List<SingleChat> singleChatsTemp = new ArrayList<>();
                Iterator chatIterator = dataSnapshot.getChildren().iterator();
                Iterator keyIterator = dataSnapshot.getChildren().iterator();

                while (keyIterator.hasNext() && chatIterator.hasNext()){
                    SingleChat singleChat = ((DataSnapshot)chatIterator.next()).getValue(SingleChat.class);
                    String key = ((DataSnapshot)keyIterator.next()).getKey();
                    singleChatsTemp.add(singleChat);
                    singleChatWithKeys.put(key, singleChat);
                }

                chats.clear();
                chats.addAll(filterChats2(singleChatWithKeys));
                privateChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<SingleChat> filterChats2(HashMap<String, SingleChat> singleChatWithKeys) {
        List<SingleChat> userChats = new ArrayList<>();
        for(String chatKey: currentUser.getSingleChatsKeys()){
            userChats.add(singleChatWithKeys.get(chatKey));
        }
        return userChats;
    }


    private List<SingleChat> filterChats(List<SingleChat> set) {
        return set;
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

    private List<SingleChat> singleChatsMock(){
        List<SingleChat> singleChats = new ArrayList<>();
        SingleChat singleChat = new SingleChat("1");
        singleChat.getParticipants().addAll(chatsMock());
        singleChats.add(singleChat);
        return singleChats;
    }

    private List<UserDTO> chatsMock(){
        List<UserDTO> userDTOS = new ArrayList<>();
        userDTOS.add(new UserDTO("Julian Rodriguez", "julian@myuni.com", "https://firebasestorage.googleapis.com/v0/b/myuni-aade3.appspot.com/o/Profile%20Images%2Fmatias.alonso%40myuni.com.jpg?alt=media&token=f0726978-8076-463e-a3c5-cf701531621f"));
        userDTOS.add(new UserDTO("Pablo Escobar", "pablo@myuni.com", "https://firebasestorage.googleapis.com/v0/b/myuni-aade3.appspot.com/o/Profile%20Images%2Fmatias.alonso%40myuni.com.jpg?alt=media&token=f0726978-8076-463e-a3c5-cf701531621f"));
        return userDTOS;
    }

    private void getUserFS(String guestUserEmail) {
        userRef = db.collection("users").document(guestUserEmail);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            currentUser = doc.toObject(User.class);
                            privateChatAdapter.setCurrentUser(currentUser.getEmail());
                            Log.i("getUserFS", doc.getId() + " => " + doc.getData());
                            retrieveAndDisplayGroups();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("getUserFS", "Error getting user");
                    }
                });
    }

}
