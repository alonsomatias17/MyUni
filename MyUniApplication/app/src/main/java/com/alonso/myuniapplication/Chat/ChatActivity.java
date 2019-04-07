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
import com.alonso.myuniapplication.business.SingleChat;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    private DatabaseReference rootRef;
    private FirebaseFirestore firebaseFirestore;

    FirebaseFirestore db;
    DocumentReference userRef;


    private User currentUser = new User();
    private User guestUser = new User("");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

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
                requestNewGroup();
                break;
            case R.id.chat_create_single_chat:
                Toast.makeText(ChatActivity.this, "Crear chat grupal",Toast.LENGTH_SHORT).show();
                requestNewSingleChat();
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

    private void requestNewSingleChat() {
        AlertDialog.Builder builder= new AlertDialog.Builder(ChatActivity.this, R.style.Alertdialog);

        //TODO: Cambiar, que elija al tutor
        builder.setTitle("Enter Chat Name:");

        final EditText chatNameField = new EditText(ChatActivity.this);
        chatNameField.setHint("Ej. Tutor de Física");

        builder.setView(chatNameField);
        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String chatName = chatNameField.getText().toString();
                if(TextUtils.isEmpty(chatName)){
                    Toast.makeText(ChatActivity.this, "Por favor escriba el nombre del chat",Toast.LENGTH_SHORT).show();

                } else {
                    createNewSingleChatForUser("julian@myuni.com", chatName);
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

    private void createNewSingleChatForUser(String guestUserEmail, final String chatName) {
        userRef = db.collection("users").document(guestUserEmail);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
                    @Override
                    public void onComplete(@NonNull Task < DocumentSnapshot > task) {
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

    private void createNewGroup(final String groupName) {
        rootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //TODO: Agregar la key al currentUser
                            updateUserFS(currentUser);
                            Toast.makeText(ChatActivity.this, "El grupo: " + groupName + " fue creado exitosamente",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createNewSingleChat(final String chatName) {
        final String key = rootRef.child("SingleChats").push().getKey();
        //TODO: Crear objeto single chat con valores reales
        SingleChat singleChat = new SingleChat();
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
                            Toast.makeText(ChatActivity.this, "El chat: " + chatName + " fue creado exitosamente",Toast.LENGTH_SHORT).show();
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
