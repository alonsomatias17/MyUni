package com.alonso.myuniapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuActionBarDrawerToggle;

    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;



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
        setNavigationLisatener();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
    }

    private void setNavigationLisatener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_account:
                        Toast.makeText(MenuActivity.this, "My Account",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(menuActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
