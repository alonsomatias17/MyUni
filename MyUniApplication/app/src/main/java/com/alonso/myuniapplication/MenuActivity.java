package com.alonso.myuniapplication;

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

    private User user;
    private UserDTO userDTO;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

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
        findUserByEmailFS();
    }

    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_account:
                        Toast.makeText(MenuActivity.this, "My Account",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(MenuActivity.this, Profile3Activity.class));

                        Intent mIntent = new Intent(MenuActivity.this, Profile3Activity.class);
                        System.out.println("MenuActivity. User info: " + user.getUserName() + " " + user.getEmail());

                        mIntent.putExtra("User", userDTO);
                        startActivity(mIntent);
                        break;
                    case R.id.nav_chat:
                        Toast.makeText(MenuActivity.this, "Chat",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_aproveed_subjects:
                        Toast.makeText(MenuActivity.this, "Info",Toast.LENGTH_SHORT).show();
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
                                if(!user.getEmail().equals("")){
                                    userDTO = new UserDTO(user.getUserName(), user.getEmail());
//                                    progressBarStatus++;
                                }

                                Log.d("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("findUserByEmailFS", "Error getting documents.", task.getException());
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


}
