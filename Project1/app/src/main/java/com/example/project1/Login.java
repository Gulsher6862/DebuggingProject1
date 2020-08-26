package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText mphone,mpassword;
    Button mlogin;
    TextView createac,forgotpassword;
    private ProgressDialog mProgressDialog;
    private String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mphone = (EditText)findViewById(R.id.input_number);
        mpassword = (EditText)findViewById(R.id.input_password);
        mlogin = (Button)findViewById(R.id.btnlogin);
        createac = (TextView)findViewById(R.id.ca);
        forgotpassword = (TextView)findViewById(R.id.forgotpass);
        mProgressDialog = new ProgressDialog(this);

    mlogin.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String phone = mphone.getText().toString().trim();
            String password = mpassword.getText().toString().trim();

            if(TextUtils.isEmpty(phone)){
                mphone.setError("phone no is required...");
                return;
            }else if(TextUtils.isEmpty(password)){
                mpassword.setError("password is required");
                return;
            }
           loginUser(phone,password);
        }
    });

    createac.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        }
    });

    }

    private void loginUser(String phone,String password){
        mProgressDialog.setTitle("Login Account");
        mProgressDialog.setMessage("checking the credentials...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        checkAccessToAccount(phone,password);
    }

    private void checkAccessToAccount(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBName).child(phone).exists()){
                    Users userData = snapshot.child(parentDBName).child(phone).getValue(Users.class);

                    if(phone.equals(userData.getPhone())){
                        if(password.equals(userData.getPassword())){
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Logged In Successfully...",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }else{
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"incorrect password...",Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"user not exists...",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}