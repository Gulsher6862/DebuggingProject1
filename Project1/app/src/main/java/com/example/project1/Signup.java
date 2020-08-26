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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Model.Users;
import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import io.paperdb.Paper;

public class Signup extends AppCompatActivity {

    EditText musername,mpassword,mphoneno,memail,mgender;
    Button msignup;
    TextView mhaveaccount;
    private ProgressDialog mProgressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("Users");


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
        mProgressDialog = new ProgressDialog(this);


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
                    mpassword.setError("required password");
                    return;
                }
                if(TextUtils.isEmpty(Email)){
                    memail.setError("required email");
                    return;
                }
                if(TextUtils.isEmpty(Gender)){
                    mgender.setError("required gender");
                    return;
                }
                if(TextUtils.isEmpty(phoneno)){
                    mphoneno.setError("required phone no");
                    return;
                }

                createAccount(username,phoneno,password);

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

    private void createAccount(String name,String phone,String password){
      mProgressDialog.setTitle("Create Account");
      mProgressDialog.setMessage("checking the credentials...");
      mProgressDialog.setCanceledOnTouchOutside(false);
      mProgressDialog.show();

      Users newUser = new Users(name,phone,password);
      userCollection.document(phone).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
           mProgressDialog.dismiss();
              Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
          }
      });

    }

}