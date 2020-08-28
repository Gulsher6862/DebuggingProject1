package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.project1.Model.Products;
import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    private ImageView productImage;
    private ElegantNumberButton mNumberButton;
    private Button addtoCart;
    private TextView productName,productPrice,productDescription;
    private String productId = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productCollection = db.collection("Products");
    private DocumentReference productRef;
    private CollectionReference cartRef = db.collection("Cart");
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mNumberButton = findViewById(R.id.number_button);
        addtoCart = findViewById(R.id.add_to_cart);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_desc);
        productId = getIntent().getStringExtra("pid");
        productRef = productCollection.document(productId);
        mProgressDialog = new ProgressDialog(this);
        
        getProductDetails(productId);

        //clicklistener on add to cart button
        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.setTitle("Add To Cart");
                mProgressDialog.setMessage("Adding product to cart, please wait...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                addtoCartList();
            }
        });
    }

    //adding product details to cart
    private void addtoCartList() {
        String saveCurrentDate,saveCurrentTime;
        Calendar dateCalendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(dateCalendar.getTime());

        Calendar timeCalendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(timeCalendar.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",mNumberButton.getNumber());

        cartRef.document(Prevelant.currentUser.getPhone()).collection("Products").document(productId).set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressDialog.dismiss();
                Toast.makeText(ProductDetails.this, "Added to Cart...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(ProductDetails.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //fetching product having id equals to productId from firestore
    private void getProductDetails(String productId) {
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productName.setText("Product : " + products.getName());
                        productPrice.setText("Price : " + products.getPrice() + "$");
                        productDescription.setText("Description : " + products.getDescription());
                        Picasso.get().load(products.getImage()).into(productImage);
                    }
                }else{
                    Toast.makeText(ProductDetails.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}