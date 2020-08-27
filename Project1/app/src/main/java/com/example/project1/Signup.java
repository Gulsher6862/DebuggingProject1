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
import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        Paper.init(this);

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

        String userPhone = Paper.book().read(Prevelant.userPhone);
        String userPassword = Paper.book().read(Prevelant.userPassword);

        if(userPhone != "" && userPassword != ""){
            if(!TextUtils.isEmpty(userPhone) && !TextUtils.isEmpty(userPassword)){
                mProgressDialog.setTitle("Already Logged In...");
                mProgressDialog.setMessage("please wait...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                checkAccess(userPhone,userPassword);
            }
        }
    }

    private void checkAccess(String phone, final String password) {
        DocumentReference docIdRef = db.collection("Users").document(phone);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mProgressDialog.dismiss();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Users currentUser = document.toObject(Users.class);
                        if(password.equals(currentUser.getPassword())){
                            Toast.makeText(Signup.this, "Successfully Logged In...", Toast.LENGTH_SHORT).show();
                            Prevelant.currentUser = currentUser;
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }else{
                            Toast.makeText(Signup.this, "wrong password entered...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "user does not exists...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(Signup.this, "Error : please try again...", Toast.LENGTH_SHORT).show();
                }
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

      Users newUser = new Users(name,phone,password,null,null);
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