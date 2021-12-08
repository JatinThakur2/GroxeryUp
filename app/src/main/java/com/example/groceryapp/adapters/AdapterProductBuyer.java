package com.example.groceryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.FilterProductBuyer;
import com.example.groceryapp.R;
import com.example.groceryapp.activity.MainBuyerActivity;
import com.example.groceryapp.activity.RegisterBuyerActivity;
import com.example.groceryapp.activity.ShopDetailsActivity;
import com.example.groceryapp.models.ModelProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterProductBuyer extends RecyclerView.Adapter<AdapterProductBuyer.HolderAdapterBuyer> implements Filterable{

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProductBuyer filter;


    public AdapterProductBuyer(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }


    @NonNull
    @Override
    public HolderAdapterBuyer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_product_buyer, parent, false);
        return new HolderAdapterBuyer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAdapterBuyer holder, int position) {

        final ModelProduct modelProduct = productList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPercent = modelProduct.getDiscountPercent();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getPrductCategory();
        String orignalPrice = modelProduct.getOrignalPrice();
        String productDescription = modelProduct.getPrductDescription();
        String productTitle = modelProduct.getPrductTitle();
        String productQuantity = modelProduct.getPrductQuantity();
        String productId = modelProduct.getPrductId();
        String timestamp = modelProduct.getTimestamp();
        String productIcon = modelProduct.getPrductIcon();

        holder.titleTv.setText(productTitle);
        holder.discountPercentTv.setText(discountPercent);
        holder.descriptionTv.setText(productDescription);
        holder.orignalPriceTv.setText("$"+orignalPrice);
        holder.discountPriceTv.setText("$"+discountPrice);
        if (discountAvailable.equals("true")){
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.discountPercentTv.setVisibility(View.VISIBLE);
            holder.orignalPriceTv.setPaintFlags(holder.orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.discountPriceTv.setVisibility(View.GONE);
            holder.discountPercentTv.setVisibility(View.GONE);
            holder.orignalPriceTv.setPaintFlags(0);
        }
        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_cart_white).into(holder.productIv);
        }
        catch (Exception e) {
            holder.productIv.setImageResource(R.drawable.ic_cart_white);
        }

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityDialog(modelProduct);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private double cost = 0;
    private double finalCost = 0;
    private double actualFinalCost = 0;
    private int quantity = 0;
    private void showQuantityDialog(ModelProduct modelProduct) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity, null);

        ImageView productIV = view.findViewById(R.id.productIV);
        final TextView titleTV = view.findViewById(R.id.titleTV);
        TextView pQuantityTV = view.findViewById(R.id.pQuantityTV);
        TextView descriptionTV = view.findViewById(R.id.descriptionTV);
        TextView discountPercentTV = view.findViewById(R.id.discountPercentTV);
        final TextView orignalPriceTV = view.findViewById(R.id.orignalPriceTV);
        TextView discountPriceTV = view.findViewById(R.id.discountPriceTV);
        final TextView finalPriceTV = view.findViewById(R.id.finalPriceTV);
        ImageButton decreaseBtn = view.findViewById(R.id.decreaseBtn);
        final TextView quantityTV = view.findViewById(R.id.quantityTV);
        ImageButton increasBtn = view.findViewById(R.id.increasBtn);
        Button continueBtn = view.findViewById(R.id.continueBtn);

        final String productId = modelProduct.getPrductId();
        String title = modelProduct.getPrductTitle();
        String productQuantity = modelProduct.getPrductQuantity();
        String description = modelProduct.getPrductDescription();
        final String discountPercent = modelProduct.getDiscountPercent();
        final String image = modelProduct.getPrductIcon();
        final String discountAvailable = modelProduct.getDiscountAvailable();
        final String category = modelProduct.getPrductCategory();
        final String orgnalPrice = modelProduct.getOrignalPrice();
        final String shopId = modelProduct.getUid();
        String price;

        if (discountAvailable.equals("true")){
            discountPercentTV.setVisibility(View.VISIBLE);
            discountPriceTV.setVisibility(View.VISIBLE);
            price = modelProduct.getDiscountPrice();
            orignalPriceTV.setPaintFlags(orignalPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            discountPercentTV.setVisibility(View.GONE);
            discountPriceTV.setVisibility(View.GONE);
            price = modelProduct.getOrignalPrice();
        }
        cost = Double.parseDouble(price.replaceAll("$", ""));
        finalCost = Double.parseDouble(price.replaceAll("$", ""));
        actualFinalCost = Double.parseDouble(orgnalPrice.replaceAll("$", ""));
        quantity = 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_cart_primary).into(productIV);
        }
        catch (Exception e){
            productIV.setImageResource(R.drawable.ic_cart_primary);
        }
        titleTV.setText(""+title);
        pQuantityTV.setText(""+productQuantity);
        descriptionTV.setText(""+description);
        discountPercentTV.setText(""+discountPercent);
        quantityTV.setText(""+quantity);
        orignalPriceTV.setText("$"+modelProduct.getOrignalPrice());
        discountPriceTV.setText("$"+modelProduct.getDiscountPrice());
        finalPriceTV.setText("$"+finalCost);

        final AlertDialog dialog = builder.create();
        dialog.show();

        increasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCost = finalCost+cost;
                actualFinalCost = Double.parseDouble(orgnalPrice)+ actualFinalCost;
                quantity++;

                finalPriceTV.setText("$"+finalCost);
                quantityTV.setText(""+quantity);
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>1) {
                    finalCost = finalCost - cost;
                    actualFinalCost = actualFinalCost - Double.parseDouble(orgnalPrice);
                    quantity--;

                    finalPriceTV.setText("$" + finalCost);
                    quantityTV.setText("" + quantity);
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleTV.getText().toString().trim();
                String price = finalPriceTV.getText().toString().trim().replace("", "");
                String quantity = quantityTV.getText().toString().trim();
                String actualPrice = "$" + actualFinalCost;

                addToCart(productId, title, price, quantity, image, discountPercent, discountAvailable, category, actualPrice, shopId);
                dialog.dismiss();
            }
        });
    }

    private void addToCart(String productId, String title,  String price, String quantity, String image_uri,
                           String discountPercent, String discountAvailable, String productCategory, String actualFinalPrice, String shopId) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (image_uri == ""){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+mAuth.getUid());
            hashMap.put("productId", productId);
            hashMap.put("profileImage","");
            hashMap.put("title",""+title);
            hashMap.put("prductCategory", ""+productCategory);
            hashMap.put("quantity",""+quantity);
            hashMap.put("discountAvailable", ""+discountAvailable);
            hashMap.put("discountPercent", ""+discountPercent);
            hashMap.put("actualPrice",""+actualFinalPrice);
            hashMap.put("finalPrice",""+price);
            hashMap.put("ShopId", ""+shopId);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(mAuth.getUid()).child("CartItem").child(shopId).child(productId).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Product Added to Cart...", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+mAuth.getUid());
            hashMap.put("productId", productId);
            hashMap.put("profileImage",""+image_uri);
            hashMap.put("title",""+title);
            hashMap.put("prductCategory", ""+productCategory);
            hashMap.put("quantity",""+quantity);
            hashMap.put("discountAvailable", ""+discountAvailable);
            hashMap.put("discountPercent", ""+discountPercent);
            hashMap.put("actualPrice",""+actualFinalPrice);
            hashMap.put("finalPrice",""+price);
            hashMap.put("ShopId", ""+shopId);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(mAuth.getUid()).child("CartItem").child(shopId).child(productId).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Item Added to cart.....", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null) {
            filter = new FilterProductBuyer(this, filterList);
        }
        return filter;
    }

    class HolderAdapterBuyer extends RecyclerView.ViewHolder{

        private ImageView productIv;
        private TextView discountPercentTv, titleTv, descriptionTv, addToCartTv, discountPriceTv, orignalPriceTv;

        public HolderAdapterBuyer(@NonNull View itemView) {
            super(itemView);

            productIv =  itemView.findViewById(R.id.productIconIV);
            discountPercentTv =  itemView.findViewById(R.id.discountPercentTV);
            titleTv =  itemView.findViewById(R.id.titleTV);
            descriptionTv =  itemView.findViewById(R.id.descriptionTV);
            addToCartTv =  itemView.findViewById(R.id.addToCartTV);
            discountPriceTv =  itemView.findViewById(R.id.discountPriceTV);
            orignalPriceTv =  itemView.findViewById(R.id.orignalPriceTV);
            productIv = itemView.findViewById(R.id.productIconIV);
        }
    }
}
