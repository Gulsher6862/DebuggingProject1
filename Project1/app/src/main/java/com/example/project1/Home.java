package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Products;
import com.example.project1.Prevelant.Prevelant;
import com.example.project1.ViewHolder.ProductViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.security.Policy;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ImageView menImage,womenImage,newArrivals,burberry,tomford,saintLaurent,prada,bvlgari,versace,dolce,christianDior,gucci,chanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        newArrivals = findViewById(R.id.imageView4);
        menImage = findViewById(R.id.imageView6);
        womenImage = findViewById(R.id.imageView5);
        burberry = findViewById(R.id.imageView7);
        tomford = findViewById(R.id.imageView8);
        saintLaurent = findViewById(R.id.imageView9);
        prada = findViewById(R.id.imageView10);
        bvlgari = findViewById(R.id.imageView11);
        versace = findViewById(R.id.imageView12);
        dolce = findViewById(R.id.imageView13);
        christianDior = findViewById(R.id.imageView14);
        gucci = findViewById(R.id.imageView15);
        chanel = findViewById(R.id.imageView16);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTv = headerView.findViewById(R.id.username);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);

        userNameTv.setText(Prevelant.currentUser.getName());
        Picasso.get().load(Prevelant.currentUser.getImage()).placeholder(R.drawable.men).into(profileImageView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set click listeners on all the image views
        newArrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewArrivalsActivity.class));
            }
        });

        menImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MenPerfumes.class));
            }
        });

        womenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WomenPerfumes.class));
            }
        });

        burberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Burberry.class));
            }
        });

        tomford.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TomFord.class));
            }
        });

        saintLaurent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SaintLaurent.class));
            }
        });

        prada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Prada.class));
            }
        });

        bvlgari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Bvlgari.class));
            }
        });

        versace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Versace.class));
            }
        });

        dolce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DolceAndGabbana.class));
            }
        });

        christianDior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChristianDior.class));
            }
        });

        gucci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Gucci.class));
            }
        });

        chanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Chanel.class));
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_cart:
                //redirect user to cart page
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
                break;
            case R.id.nav_order:
                //redirect user to my orders page
                startActivity(new Intent(getApplicationContext(),MyOrders.class));
                break;
            case R.id.nav_settings:
                //redirect user to settings page
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.nav_policy:
                //redirect user to policies page
                startActivity(new Intent(getApplicationContext(),Policies.class));
                break;
            case R.id.nav_card:
                //redirect user to add card page
                startActivity(new Intent(getApplicationContext(),AddCard.class));
                break;
            case R.id.nav_logout:
                //use paper library to destroy the current user info stored in android storage
                Paper.book().destroy();
                //redirect user to signup page
                Intent intent = new Intent(Home.this,Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //show toast message
                Toast.makeText(this, "Logged Out...", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}





