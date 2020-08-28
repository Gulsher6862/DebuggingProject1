package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project1.Model.Orders;
import com.example.project1.Prevelant.Prevelant;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyOrders extends AppCompatActivity {

    private Button shopMore;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("Orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        shopMore = findViewById(R.id.shop_more_btn);
        mRecyclerView = findViewById(R.id.orders_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        shopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<Orders> options = new FirestoreRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef.document(Prevelant.currentUser.getPhone()).collection("My Orders"),Orders.class)
                .build();

        FirestoreRecyclerAdapter<Orders,OrdersViewHolder> adapter = new FirestoreRecyclerAdapter<Orders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull Orders model) {
                holder.userName.setText("Name : " + model.getName());
                holder.userPhone.setText("Phone : " + model.getPhone());
                holder.userAddress.setText("Address : " + model.getAddress() + ", " + model.getCity());
                holder.userTotalPrice.setText("Total Price : " + model.getTotalAmount() + "$");
                holder.userDate.setText("Order Date : " + model.getDate());
                holder.userTime.setText("Order Time : " + model.getTime());
            }

            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new OrdersViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName,userPhone,userTotalPrice,userAddress,userDate,userTime;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_name);
            userPhone = itemView.findViewById(R.id.order_phone);
            userTotalPrice = itemView.findViewById(R.id.order_price);
            userAddress = itemView.findViewById(R.id.order_address);
            userDate = itemView.findViewById(R.id.order_date);
            userTime = itemView.findViewById(R.id.order_time);
        }
    }
}