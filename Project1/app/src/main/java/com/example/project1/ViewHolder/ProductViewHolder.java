package com.example.project1.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Interface.ItemClickListner;
import com.example.project1.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName,productCategory,productPrice;
    public ImageView productImage;
    public ItemClickListner listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        productCategory = itemView.findViewById(R.id.product_category);
        productPrice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListner listener){
       this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
