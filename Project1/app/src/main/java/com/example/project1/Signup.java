package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    EditText musername,mpassword,mphoneno,memail,mgender;
    Button msignup;

    TextView mhaveaccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        musername = (EditText)findViewById(R.id.input_usernmae);
        mpassword = (EditText)findViewById(R.id.input_password);
        mphoneno = (EditText)findViewById(R.id.input_phn);
        mgender = (EditText)findViewById(R.id.input__gender);
         memail = (EditText)findViewById(R.id.input_email);
        mhaveaccount = (TextView)findViewById(R.id.alreadaccount);
        msignup = (Button)findViewById(R.id.btnSignUp);


        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String username = musername.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                String Email = memail.getText().toString().trim();
                 String phoneno = mphoneno.getText().toString().trim();
                 String Gender = mgender.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    musername.setError("required username");
                    return;
                };
                if (TextUtils.isEmpty(password)){
                    mpassword.setError("Required Password");
                    return;
                }
                if(TextUtils.isEmpty(Email)){
                    memail.setError("Required Email");
                    return;
                }
                if(TextUtils.isEmpty(phoneno)){
                    mphoneno.setError("Required Phone no");
                    return;
                }
                if(TextUtils.isEmpty(Gender)){
                    mgender.setError("Required Gender");
                    return;
                }
                startActivity(new Intent(getApplicationContext(),Home.class));

            }
        });

        mhaveaccount.setOnClickListener(new View.OnClickListener() {
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

   
}