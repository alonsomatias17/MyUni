package com.alonso.myuniapplication.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.SingleChat;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CreateSingleChatActivity extends AppCompatActivity {

    private Spinner subjectSP;
    private Spinner tutorSP;
    private Button createSingleChatBT;

    private User currentUser = new User();
    HashMap<Integer,  List<String>> tutorsByKey = new HashMap<>();
    ArrayAdapter<Subject> subjectAdapter;


    private DatabaseReference tutorsRef;
    private DatabaseReference rootRef;
    DocumentReference userRef;
    FirebaseFirestore db;
    private FirebaseFirestore firebaseFirestore;


    private User guestUser = new User("");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_single_chat);

        tutorsRef = FirebaseDatabase.getInstance().getReference().child("Tutors");
        rootRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent mIntent = getIntent();
        currentUser = mIntent.getParcelableExtra("NewSingleChatUser");

        subjectSP  = (Spinner) findViewById(R.id.subjectSpinner);
        tutorSP  = (Spinner) findViewById(R.id.tutorSpinner);
        createSingleChatBT = (Button) findViewById(R.id.createChatButton);

        subjectSpinnerListener();
        createSingleChatListener();
        searchForTutors();

    }

    private void subjectSpinnerListener() {
        subjectSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = (Subject) subjectSP.getSelectedItem();

                ArrayAdapter<String> tutorAdapter = new ArrayAdapter<>(CreateSingleChatActivity.this,
                        android.R.layout.simple_spinner_item, tutorsByKey.get(subject.getCode()));
                tutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tutorSP.setAdapter(tutorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createSingleChatListener() {
        createSingleChatBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject subject = (Subject) subjectSP.getSelectedItem();
                String tutor = (String) tutorSP.getSelectedItem();
                createNewSingleChatForUser(tutor, subject.toString());
            }
        });
    }

    private void searchForTutors() {
        for(final Subject onGoingSubject: currentUser.getOnGoingSubjects()){
            getTutors(onGoingSubject);
        }
        subjectAdapter = new ArrayAdapter<>(CreateSingleChatActivity.this,
                android.R.layout.simple_spinner_item, currentUser.getOnGoingSubjects());
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSP.setAdapter(subjectAdapter);
    }

    private void getTutors(final Subject onGoingSubject) {
        tutorsRef.child(Integer.toString(onGoingSubject.getCode()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> tutorsKeys = new ArrayList<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    String singleChat = ((DataSnapshot)iterator.next()).getValue(String.class);
                    tutorsKeys.add(singleChat);
                }
                tutorsByKey.put(onGoingSubject.getCode(), tutorsKeys);
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNewSingleChatForUser(String guestUserEmail, final String chatName) {
        userRef = db.collection("users").document(guestUserEmail);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            guestUser = doc.toObject(User.class);
                            Log.i("getUserFS", doc.getId() + " => " + doc.getData());
                            createNewSingleChat(chatName);
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

    private void createNewSingleChat(final String chatName) {
        final String key = rootRef.child("SingleChats").push().getKey();
        //TODO: Crear objeto single chat con valores reales
        SingleChat singleChat = new SingleChat(key);
        singleChat.getParticipants().add(userToUserDTO(currentUser));
        singleChat.getParticipants().add(userToUserDTO(guestUser));

        rootRef.child("SingleChats").child(key).setValue(singleChat)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //TODO: Agregar la key al currentUser
                            currentUser.getSingleChatsKeys().add(key);
                            updateUserFS(currentUser);

                            //TODO: Chequear si ya lo trajo o hacer una task
                            guestUser.getSingleChatsKeys().add(key);
                            updateUserFS(guestUser);

                            Intent ChatIntent = new Intent(CreateSingleChatActivity.this, ChatActivity.class);
                            ChatIntent.putExtra("UserToChats", currentUser);
                            startActivity(ChatIntent);

                            Toast.makeText(CreateSingleChatActivity.this,
                                    "El chat: " + chatName + " fue creado exitosamente",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getUserName(), user.getEmail(), user.getProfileImageUri());
    }

    private void updateUserFS(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("users")
                        .document(user.getEmail())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("ChatActivity", "User " + user.getEmail() + " updated correctly");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ChatActivity", "Error updating user " + user.getEmail(), e);
                            }
                        });
            }
        }).start();
    }

}
