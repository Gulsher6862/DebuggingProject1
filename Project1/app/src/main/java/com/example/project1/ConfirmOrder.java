package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity {

    private EditText name,phone,address,city;
    private Button placeOrder;
    private TextView orderTotal;
    private String totalPrice = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Cart");
    private CollectionReference orderRef = db.collection("Orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getSupportActionBar().setTitle("Confirm Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        placeOrder = findViewById(R.id.place_order_btn);
        name = findViewById(R.id.shipment_name);
        phone = findViewById(R.id.shipment_number);
        address = findViewById(R.id.shipment_address);
        city = findViewById(R.id.shipment_city);
        orderTotal = findViewById(R.id.order_total);
        totalPrice = getIntent().getStringExtra("Total Price");

        orderTotal.setText("Order Total : " + totalPrice + "$");

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkShippingDetails();
            }
        });
    }

    private void checkShippingDetails() {
        if(Integer.valueOf(totalPrice) == 0){
            Toast.makeText(this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("name is required...");
        }else if(TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("number is required...");
        }else if(TextUtils.isEmpty(address.getText().toString())){
            address.setError("number is required...");
        }else if(TextUtils.isEmpty(city.getText().toString())){
            city.setError("number is required...");
        }else{
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentDate,saveCurrentTime;
        Calendar dateCalendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(dateCalendar.getTime());

        Calendar timeCalendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(timeCalendar.getTime());
        String orderId = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("orderId",orderId);
        orderMap.put("totalAmount",totalPrice);
        orderMap.put("name",name.getText().toString());
        orderMap.put("phone",phone.getText().toString());
        orderMap.put("address",address.getText().toString());
        orderMap.put("city",city.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);

        orderRef.document(Prevelant.currentUser.getPhone()).collection("My Orders").document(orderId).set(orderMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                cartRef.document(Prevelant.currentUser.getPhone()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Order Placed...",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MyOrders.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfirmOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}