package com.alonso.myuniapplication.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.adapters.TabsAccessorAdapter;
import com.alonso.myuniapplication.business.GroupChat;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.User;
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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    private DatabaseReference rootRef;
    private FirebaseFirestore firebaseFirestore;

    FirebaseFirestore db;
    DocumentReference userRef;
    private DatabaseReference onGoingSubjectRef;



    private User currentUser = new User();

    List<String> usersKeys = new ArrayList<>();
    List<User> users = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        onGoingSubjectRef = FirebaseDatabase.getInstance().getReference().child("OnGoingSubjects");


        mToolbar = (Toolbar) findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mis Chats");

        myViewPager = (ViewPager) findViewById(R.id.chat_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.chat_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        Intent mIntent = getIntent();
        currentUser = mIntent.getParcelableExtra("UserToChats");
        //TODO: Agregar progress bar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.chat_create_group:
                Toast.makeText(ChatActivity.this, "Crear chat grupal",Toast.LENGTH_SHORT).show();
                requestNewGroup2();
                break;
            case R.id.chat_create_single_chat:
                if(currentUser.getOnGoingSubjects().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Debes tener al menos una materia en curso para tener un tutor o crear un grupo de estudio", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Crear chat individual",Toast.LENGTH_SHORT).show();
                    Intent createSCIntent = new Intent(ChatActivity.this, CreateSingleChatActivity.class);
                    createSCIntent.putExtra("NewSingleChatUser", currentUser);
                    startActivity(createSCIntent);
                }
                break;
        }
        return true;
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder= new AlertDialog.Builder(ChatActivity.this, R.style.Alertdialog);
        builder.setTitle("Enter Group Name:");

        final EditText groupNameField = new EditText(ChatActivity.this);
        groupNameField.setHint("Ej. Física I");
        builder.setView(groupNameField);
        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(ChatActivity.this, "Por favor escriba el nombre del grupo",Toast.LENGTH_SHORT).show();

                } else {
                    createNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void requestNewGroup2() {
        String[] subjectsArray = new String[currentUser.getOnGoingSubjects().size()];
        subjectsArray = getSubjectsAsStringArray(subjectsArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elije una materia para crear un grupo:");
        builder.setItems(subjectsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                createGroup(currentUser.getOnGoingSubjects().get(pos));
            }
        });
        builder.show();
    }

    private void createGroup(Subject subject) {
        //TODO si ya existe el grupo agrego al nuevo usuario
        getUserAndCreateGroup(subject);
    }

    private void getUserAndCreateGroup(final Subject subject) {
        onGoingSubjectRef.child(Integer.toString(subject.getCode()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        while(iterator.hasNext()){
                            String user = ((DataSnapshot)iterator.next()).getValue(String.class);
                            usersKeys.add(user);
                        }
                        createNewGroup(subject.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @NonNull
    private String[] getSubjectsAsStringArray(String[] subjectsArray) {
        List<String> subjectsString = new ArrayList<>();

        for (Subject subject: currentUser.getOnGoingSubjects()){
            subjectsString.add(subject.toString());
        }

        subjectsArray = subjectsString.toArray(subjectsArray);
        return subjectsArray;
    }

    private void createNewGroup(final String groupName) {
        currentUser.getGroupChatsKeys().add(groupName);
        GroupChat groupChat = new GroupChat();
        groupChat.setParticipants(usersKeys);

        rootRef.child("Groups").child(groupName).setValue(groupChat)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //TODO: Agregar la key al currentUser
//                            updateCurrentUserFS(currentUser);
//                            updateUser(currentUser.getEmail(), groupName);
                            updateCurrentUserFS(currentUser);
                            updateUsers(groupName);

                            Toast.makeText(ChatActivity.this, "El grupo: " + groupName + " fue creado exitosamente",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUser(String userEmail, final String groupName) {
        /*firebaseFirestore.collection("usersKeys")
                .document(userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    user.getGroupChatsKeys().add(groupName);
                    Log.i("getUserFS", document.getId() + " => " + document.getData());
                } else {
                    Log.e("getUserFS", "Error getting documents.", task.getException());
                }
            }
        });*/

        Map<String, Object> data = new HashMap<>();
        List<String> groupKeys = new ArrayList<>();
        groupKeys.add(groupName);
        data.put("groupChatsKeys", groupKeys);

        firebaseFirestore.collection("users")
                .document(userEmail)
                .set(data, SetOptions.merge());
    }

    private void updateCurrentUserFS(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("users")
                        .document(user.getEmail())
                        .set(user)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ChatActivity", "Error updating user " + user.getEmail(), e);
                            }
                        });
            }
        }).start();
    }

    private void getUserFS(String email){
        firebaseFirestore.collection("users")
                .document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    users.add(document.toObject(User.class));
                } else {
                    Log.e("getUserFS", "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void updateUsers(String groupName){
        for(String userEmail: usersKeys){
            getAndUpdateUser(userEmail, groupName);
        }
    }

    private void getAndUpdateUser(String userEmail, final String groupName) {
        firebaseFirestore.collection("users")
                .document(userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    user.getGroupChatsKeys().add(groupName);
                    updateCurrentUserFS(user);
                } else {
                    Log.e("getUserFS", "Error getting documents.", task.getException());
                }
            }
        });
    }
}
