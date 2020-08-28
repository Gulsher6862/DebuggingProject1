package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Model.Cart;
import com.example.project1.Prevelant.Prevelant;
import com.example.project1.ViewHolder.CartViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button checkoutButton;
    private TextView totalAmount;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mRecyclerView = findViewById(R.id.cart_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        checkoutButton = findViewById(R.id.checkout_btn);
        totalAmount = findViewById(R.id.total_price);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirestoreRecyclerOptions<Cart> options = new FirestoreRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.document(Prevelant.currentUser.getPhone()).collection("Products"),Cart.class).build();

        FirestoreRecyclerAdapter<Cart, CartViewHolder> adapter = new FirestoreRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
               holder.txtProductQuantity.setText("Quantity : " + model.getQuantity());
               holder.txtProductPrice.setText(model.getPrice());
               holder.txtProductName.setText(model.getName());

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       CharSequence options[] = new CharSequence[]{
                            "Edit","Remove"
                       };
                       final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                       builder.setTitle("Cart Options");
                       builder.setItems(options, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               //if edit option is selected
                              if(i == 0){
                                  Intent intent = new Intent(getApplicationContext(),ProductDetails.class);
                                  intent.putExtra("pid",model.getPid());
                                  startActivity(intent);
                              }
                              //if remove option is selected
                               if(i == 1){
                                   cartRef.document(Prevelant.currentUser.getPhone()).collection("Products").document(model.getPid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(CartActivity.this, "product removed", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               }
                           }
                       });
                       builder.show();
                   }
               });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}