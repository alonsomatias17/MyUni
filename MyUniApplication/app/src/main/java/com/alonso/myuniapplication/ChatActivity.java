package com.alonso.myuniapplication;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alonso.myuniapplication.adapters.TabsAccessorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mis Chats");

        myViewPager = (ViewPager) findViewById(R.id.chat_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.chat_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
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

    private void createNewGroup(final String groupName) {
        rootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ChatActivity.this, "El grupo: " + groupName + " fue creado exitosamente",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
