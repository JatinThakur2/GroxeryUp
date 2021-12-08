package com.example.groceryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.FilterProduct;
import com.example.groceryapp.FilterProductBuyer;
import com.example.groceryapp.R;
import com.example.groceryapp.activity.EditProductActivity;
import com.example.groceryapp.models.ModelProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.HolderProductSeller> implements Filterable {

    private android.content.Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProduct filter;

    public AdapterProductSeller(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller, parent, false);
        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {
        final ModelProduct modelProduct = productList.get(position);
        String id = modelProduct.getPrductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPer = modelProduct.getDiscountPercent();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getPrductCategory();
        String productIcon = modelProduct.getPrductIcon();
        String productQuantity = modelProduct.getPrductQuantity();
        String productDescription = modelProduct.getPrductDescription();
        String productTitle = modelProduct.getPrductTitle();
        String orignalPrice = modelProduct.getOrignalPrice();
        String timestamp = modelProduct.getTimestamp();

        holder.titleTv.setText(productTitle);
        holder.quantityTv.setText(productQuantity);
        holder.discountPercentTv.setText(discountPer);
        holder.discountedPriceTv.setText("$ "+discountPrice);
        holder.orignalPriceTv.setText("$ "+orignalPrice);
        if (discountAvailable.equals("true")){
            holder.discountedPriceTv.setVisibility(View.VISIBLE);
            holder.discountPercentTv.setVisibility(View.VISIBLE);
            holder.orignalPriceTv.setPaintFlags(holder.orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.discountedPriceTv.setVisibility(View.GONE);
            holder.discountPercentTv.setVisibility(View.GONE);
            holder.orignalPriceTv.setPaintFlags(0);
        }
        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_cart_primary).into(holder.productIv);
        }
        catch (Exception e) {
            holder.productIv.setImageResource(R.drawable.ic_cart_primary);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailBotten(modelProduct);
            }
        });

    }

    private void detailBotten(ModelProduct modelProduct) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_details_seller, null);
        bottomSheetDialog.setContentView(view);

        ImageButton backBtn = view.findViewById(R.id.backBtn),
                editBtn = view.findViewById(R.id.editBtn),
                deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageView productIconIv= view.findViewById(R.id.productIV);
        TextView discountPerTv = view.findViewById(R.id.discountPercTV),
                titleTV = view.findViewById(R.id.titleTV),
                descriptionTv = view.findViewById(R.id.descriptionTV),
                categoryTv = view.findViewById(R.id.categoryTV),
                quantityTv = view.findViewById(R.id.quantityTV),
                discountPriceTv = view.findViewById(R.id.discountPriceTV),
                orignalPriceTv = view.findViewById(R.id.orignalPriceTV);

        final String id = modelProduct.getPrductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPer = modelProduct.getDiscountPercent();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getPrductCategory();
        String productIcon = modelProduct.getPrductIcon();
        String productQuantity = modelProduct.getPrductQuantity();
        String productDescription = modelProduct.getPrductDescription();
        final String productTitle = modelProduct.getPrductTitle();
        String orignalPrice = modelProduct.getOrignalPrice();
        String timestamp = modelProduct.getTimestamp();

        titleTV.setText(productTitle);
        descriptionTv.setText(productDescription);
        quantityTv.setText(productQuantity);
        categoryTv.setText(productCategory);
        discountPerTv.setText(discountPer);
        discountPriceTv.setText("$"+discountPrice);
        orignalPriceTv.setText("$"+orignalPrice);

        if (discountAvailable.equals("true")){
            discountPriceTv.setVisibility(View.VISIBLE);
            discountPerTv.setVisibility(View.VISIBLE);
            orignalPriceTv.setPaintFlags(orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            discountPriceTv.setVisibility(View.GONE);
            discountPerTv.setVisibility(View.GONE);
            
        }
        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_cart_white).into(productIconIv);
        }
        catch (Exception e) {
            productIconIv.setImageResource(R.drawable.ic_cart_white);
        }

        bottomSheetDialog.show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("productId", id);
                context.startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE")
                        .setMessage("Sure want to delete product " + productTitle + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(id);
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void deleteProduct(String id){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product Deleted...", Toast.LENGTH_SHORT).show();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder{

        private ImageView productIv;
        private TextView discountPercentTv, titleTv, quantityTv, discountedPriceTv, orignalPriceTv;

        public HolderProductSeller(@NonNull View itemView) {
            super(itemView);

            productIv = itemView.findViewById(R.id.productIconIV);
            discountPercentTv = itemView.findViewById(R.id.discountPercentTV);
            titleTv = itemView.findViewById(R.id.titleTV);
            quantityTv = itemView.findViewById(R.id.quantityTV);
            discountedPriceTv = itemView.findViewById(R.id.discountPriceTV);
            orignalPriceTv = itemView.findViewById(R.id.orignalPriceTV);
        }
    }
}
