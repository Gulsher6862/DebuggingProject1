package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Model.Users;
import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    EditText mphone,mpassword;
    Button mlogin;
    TextView createac,forgotpassword,admin,notAdmin;
    private CheckBox rememberMe;
    private ProgressDialog mProgressDialog;
    private String parentDBName = "Users";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mphone = (EditText)findViewById(R.id.input_number);
        mpassword = (EditText)findViewById(R.id.input_password);
        mlogin = (Button)findViewById(R.id.btnlogin);
        admin = findViewById(R.id.tv_admin);
        notAdmin = findViewById(R.id.tv_not_admin);
        createac = (TextView)findViewById(R.id.ca);
        forgotpassword = (TextView)findViewById(R.id.forgotpass);
        mProgressDialog = new ProgressDialog(this);
        rememberMe = findViewById(R.id.remember_me);

        Paper.init(this);

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

    admin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mlogin.setText("Admin Login");
            admin.setVisibility(View.GONE);
            notAdmin.setVisibility(View.VISIBLE);
            parentDBName = "Admins";
        }
    });

    notAdmin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mlogin.setText("Login");
            admin.setVisibility(View.VISIBLE);
            notAdmin.setVisibility(View.GONE);
            parentDBName = "Users";
        }
    });

    }

    private void loginUser(String phone,String password){
        mProgressDialog.setTitle("Login Account");
        mProgressDialog.setMessage("checking the credentials...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        checkAccess(phone,password);

    }

    private void checkAccess(String phone, final String password){

        DocumentReference docIdRef = db.collection(parentDBName).document(phone);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mProgressDialog.dismiss();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                     Users currentUser = document.toObject(Users.class);
                     if(password.equals(currentUser.getPassword())){
                         if(parentDBName.equals("Users")){
                             Toast.makeText(Login.this, "Successfully Logged In...", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(),Home.class));
                         }else{
                             Toast.makeText(Login.this, "Welcome Admin...", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(),AdminPanel.class));
                         }
                     }else{
                         Toast.makeText(Login.this, "wrong password entered...", Toast.LENGTH_SHORT).show();
                     }
                    } else {
                        Toast.makeText(Login.this, "user does not exists...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(Login.this, "Error : please try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}