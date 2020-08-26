package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AdminPanel extends AppCompatActivity {

    Button addButton;
    EditText productName,productDesc,productPrice;
    ImageView productImage;
    RadioGroup rdg;
    RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        addButton = findViewById(R.id.add_product);
        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_desc);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);
        rdg = findViewById(R.id.rdg);
        rb = findViewById(rdg.getCheckedRadioButtonId());

    }
}