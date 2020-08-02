package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Signup extends AppCompatActivity {


Button btnsignup;
TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnsignup = (Button)findViewById(R.id.btnSignUp);
        login = (TextView)findViewById(R.id.alreadaccount);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlogin();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlog();
            }
        });
    }

    private void openlog() {
        Intent intent = new Intent(Signup.this,Login.class);
        startActivity(intent);
    }

    private void openlogin() {
        Intent intent = new Intent(Signup.this, Home.class);
        startActivity(intent);
    }
}