package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button mlogin;
    TextView createac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    mlogin = (Button)findViewById(R.id.btnlogin);
    createac = (TextView)findViewById(R.id.ca);

    mlogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openhome();
        }
    });
    createac.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            opensign();
        }
    });

    }

    private void opensign() {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
    }

    private void openhome() {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }

}