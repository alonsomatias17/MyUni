package com.alonso.myuniapplication.Chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alonso.myuniapplication.Chat.GroupChatActivity;
import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private View groupFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    private DatabaseReference groupRef;
    DocumentReference userRef;
    private User user;


    private ArrayList<String> groups = new ArrayList<>();

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = firebaseFirestore.collection("users").document(firebaseUser.getEmail());

        initializeFields();
        userListenerFS();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentGroupName = parent.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(groupChatIntent);
            }
        });
        return groupFragmentView;
    }

    private void userListenerFS() {
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User.class);
                            Log.i("getUserFS", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            groups.clear();
                            groups.addAll(user.getGroupChatsKeys());
                            arrayAdapter.notifyDataSetChanged();

                            System.out.println("Current data: " + documentSnapshot.getData());
                        } else {
                            System.out.print("Current data: null");
                            Log.e("getUserFS", "Error getting documents.");

                        }
                    }
                });
    }

   /*
   * Database listener and list modification
   private void retrieveAndDisplayGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add( ((DataSnapshot)iterator.next()).getKey() );
                }
                groups.clear();
                groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void initializeFields() {
        listView = (ListView) groupFragmentView.findViewById(R.id.list_view_groups);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(arrayAdapter);
    }

}
