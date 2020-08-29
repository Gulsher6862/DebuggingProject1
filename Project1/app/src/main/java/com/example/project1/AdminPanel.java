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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminPanel extends AppCompatActivity {

    Button addButton;
    EditText productName,productDesc,productPrice;
    ImageView productImage;
    RadioGroup rdg;
    RadioButton rb,rbMen,rbWomen;
    private String saveCurrentDate,saveCurrentTime,productKey,downloadImageUrl;
    private String category,description,price,name;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private static int s;
    private ProgressDialog mProgressDialog;
    private StorageReference productImageRef;
    private StorageReference storageRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productCollection = db.collection("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        addButton = findViewById(R.id.add_product);
        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_desc);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);
        rbMen = findViewById(R.id.rb_men);
        rbWomen = findViewById(R.id.rb_women);
        mProgressDialog = new ProgressDialog(this);
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        storageRef = FirebaseStorage.getInstance().getReference();

        //click listener for adding image
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = productName.getText().toString().trim();
                 description = productDesc.getText().toString().trim();
                 price = productPrice.getText().toString().trim();

                 //checking for fields are not empty
                if(imageUri == null){
                    Toast.makeText(getApplicationContext(),"image is required...",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(name)){
                    productName.setError("name is required...");
                    return;
                }else if(TextUtils.isEmpty(description)){
                    productDesc.setError("description is required...");
                    return;
                }else if(TextUtils.isEmpty(price)){
                    productPrice.setError("price is required...");
                    return;
                }else if(!rbMen.isChecked() && !rbWomen.isChecked()){
                    Toast.makeText(AdminPanel.this, "Category is required...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rbMen.isChecked()){
                    category = rbMen.getText().toString().trim();
                }else{
                    category = rbWomen.getText().toString().trim();
                }

                //method to upload product information to firestore
                storeProductInfo();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,galleryPick);
    }

    private void storeProductInfo(){

        //show progress dialog when add button is clicked
        mProgressDialog.setTitle("Uploading Image");
        mProgressDialog.setMessage("please wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //using calendar class to save current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(AdminPanel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                //creating reference to firebase storage
                StorageReference pathReference = storageRef.child("Product Images/"+imageUri.getLastPathSegment() + productKey + ".jpg");

                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //getting download url from firebase storage
                        downloadImageUrl = uri.toString();
                        //save details to firestore
                        saveProductInfoToDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminPanel.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void saveProductInfoToDatabase(){

        //create hashmap to store product details
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("price",price);
        productMap.put("name",name);
        productMap.put("category",category);

        //set hashmap to firestore reference
        productCollection.document(productKey).set(productMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Toast.makeText(AdminPanel.this, "product added successfully...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AdminPanel.class));
                    finish();
                }else{
                    mProgressDialog.dismiss();
                    Toast.makeText(AdminPanel.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode == RESULT_OK && data != null){
           imageUri = data.getData();
           productImage.setImageURI(imageUri);

        }
    }
}