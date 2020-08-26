package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText musername,mpassword;
    Button mlogin;
    TextView createac,forgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        musername = (EditText)findViewById(R.id.input_usernmae);
        mpassword = (EditText)findViewById(R.id.input_password);
        mlogin = (Button)findViewById(R.id.btnlogin);
        createac = (TextView)findViewById(R.id.ca);
        forgotpassword = (TextView)findViewById(R.id.forgotpass);

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
        Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }

}