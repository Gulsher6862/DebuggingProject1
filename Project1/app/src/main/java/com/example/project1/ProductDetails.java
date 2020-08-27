package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.project1.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {

    private ImageView productImage;
    private ElegantNumberButton mNumberButton;
    private TextView productName,productPrice,productDescription;
    private String productId = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productCollection = db.collection("Products");
    private DocumentReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mNumberButton = findViewById(R.id.number_button);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_desc);
        productId = getIntent().getStringExtra("pid");
        productRef = productCollection.document(productId);
        
        getProductDetails(productId);
    }

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