package com.alonso.myuniapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuActionBarDrawerToggle;
    private NavigationView navigationView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuActionBarDrawerToggle = new ActionBarDrawerToggle(this, menuDrawerLayout, R.string.open, R.string.close);

        menuDrawerLayout.addDrawerListener(menuActionBarDrawerToggle);
        menuActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        setNavigationListener();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        findUserByEmailFS();
    }

    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_account:
                        Toast.makeText(MenuActivity.this, "Mi Perfil",Toast.LENGTH_SHORT).show();

                        Intent accountIntent = new Intent(MenuActivity.this, Profile3Activity.class);
                        accountIntent.putExtra("User", user);
                        startActivity(accountIntent);
                        break;
                    case R.id.nav_chat:
                        Toast.makeText(MenuActivity.this, "Chat",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_asign_aprobadas:
                        Toast.makeText(MenuActivity.this, "Mis Asignaturas",Toast.LENGTH_SHORT).show();

                        Intent ASIntent = new Intent(MenuActivity.this, ApprovedSubjectsActivity.class);
                        ASIntent.putExtra("UserToApSubject", user);
                        startActivity(ASIntent);
                        break;
                    case R.id.nav_ongoing_subjects:
                        Toast.makeText(MenuActivity.this, "Info",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_information:
                        Toast.makeText(MenuActivity.this, "Info",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;


            }
        });
    }

    private void findUserByEmailFS(){
        firebaseFirestore.collection("users")
                .whereEqualTo("email", firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(User.class);
                                Log.i("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("findUserByEmailFS", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(menuActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        findUserByEmailFS();
    }
}
