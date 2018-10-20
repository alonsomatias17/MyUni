package com.alonso.myuniapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuActionBarDrawerToggle = new ActionBarDrawerToggle(this, menuDrawerLayout, R.string.open, R.string.close);

        menuDrawerLayout.addDrawerListener(menuActionBarDrawerToggle);
        menuActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, new MainMenuFragment());
        fragmentTransaction.commit();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(menuActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_account:
                this.startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_asignaturas_aprobadas:
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.nav_asignaturas_curso:
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.nav_info:
                // another startActivity, this is for item with id "menu_item2"
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return true;
    }
}
