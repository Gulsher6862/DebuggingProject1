package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_order:
                startActivity(new Intent(Home.this,MyOrders.class));
                break;
            case R.id.nav_policy:
                startActivity(new Intent(Home.this,MyPolicies.class));
                break;
            case R.id.nav_card:
                startActivity(new Intent(Home.this,MyCards.class));
                break;
            case R.id.nav_share:
                startActivity(new Intent(Home.this,Share.class));
                break;
            case R.id.nav_logout:
                Toast.makeText(Home.this,"Successfully Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this,Logout.class));
                break;
        }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

 public void onBackPressed(){
     if (drawer.isDrawerOpen(GravityCompat.START)) {
         drawer.closeDrawer(GravityCompat.START);
     } else {
         super.onBackPressed();
     }
 }
 }





