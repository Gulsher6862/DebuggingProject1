package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Model.Users;
import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    EditText name,phone,address;
    TextView changePic,closeSettings,updateSettings;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("Users");
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference mStorageReference;
    private String checker = "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView = findViewById(R.id.settings_profile_image);
        name = findViewById(R.id.settings_name);
        phone = findViewById(R.id.settings_phone);
        address = findViewById(R.id.settings_address);
        changePic = findViewById(R.id.profile_image_change);
        closeSettings = findViewById(R.id.close_settings);
        updateSettings = findViewById(R.id.update_settings);
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        userInfoDisplay(profileImageView,name,phone,address);

        closeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")){
                     userInfoSaved();
                }else{
                     updateOnlyUserInfo();
                }
            }
        });

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "error : try again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",name.getText().toString());
        userMap.put("address",address.getText().toString());
        userMap.put("phoneOrder",phone.getText().toString());

        userCollection.document(Prevelant.currentUser.getPhone()).update(userMap);
        startActivity(new Intent(getApplicationContext(),Home.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("phone is required...");
        }
        else if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("name is required...");
        }
        else if(TextUtils.isEmpty(address.getText().toString())){
            address.setError("address is required...");
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("updating info, please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = mStorageReference.child(Prevelant.currentUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("name",name.getText().toString());
                        userMap.put("address",address.getText().toString());
                        userMap.put("phoneOrder",phone.getText().toString());
                        userMap.put("image",myUrl);

                        userCollection.document(Prevelant.currentUser.getPhone()).update(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }else{
            Toast.makeText(this, "image is not selected...", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText name, final EditText phone, final EditText address) {
        DocumentReference docIdRef = userCollection.document(Prevelant.currentUser.getPhone());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("image") != null){
                            String image = document.get("image").toString();
                            String userName = document.get("name").toString();
                            String userPhone = document.get("phone").toString();
                            String userAddress = document.get("address").toString();
                            Picasso.get().load(image).into(profileImageView);
                            name.setText(userName);
                            phone.setText(userPhone);
                            address.setText(userAddress);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "user does not exists...", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}