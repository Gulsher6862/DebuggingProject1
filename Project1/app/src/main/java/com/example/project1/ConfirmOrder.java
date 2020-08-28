package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmOrder extends AppCompatActivity {

    private EditText name,phone,address,city;
    private Button placeOrder;
    private TextView orderTotal;
    private String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        placeOrder = findViewById(R.id.place_order_btn);
        name = findViewById(R.id.shipment_name);
        phone = findViewById(R.id.shipment_number);
        address = findViewById(R.id.shipment_address);
        city = findViewById(R.id.shipment_city);
        orderTotal = findViewById(R.id.order_total);
        totalPrice = getIntent().getStringExtra("Total Price");

        orderTotal.setText("Order Total : " + totalPrice + "$");
        
    }
}