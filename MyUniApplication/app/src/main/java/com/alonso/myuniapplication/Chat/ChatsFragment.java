package com.alonso.myuniapplication.Chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.Contact;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View privateChatsView;
    private RecyclerView chats;

    private DatabaseReference chatsRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         privateChatsView = inflater.inflate(R.layout.fragment_chats, container, false);

//         chatsRef = FirebaseDatabase.getInstance().getReference().child().child();

         chats = (RecyclerView) privateChatsView.findViewById(R.id.single_chat_list);
         chats.setLayoutManager(new LinearLayoutManager(getContext()));

         return privateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO: cambiar a una clase que use realmente
        /*FirebaseRecyclerOptions<Contact>  options = new FirebaseRecyclerOptions.Builder<Contact>()
                .setQuery();*/

    }
}
