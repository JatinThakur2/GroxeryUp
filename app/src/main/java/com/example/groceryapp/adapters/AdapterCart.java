package com.example.groceryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.models.ModelCart;
import com.example.groceryapp.models.ModelProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.HolderCart> {

    private Context context;
    public ArrayList<ModelCart> cartList;
    int totel = 0;

    public AdapterCart(Context context, ArrayList<ModelCart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public HolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_cart, parent, false);
        return new HolderCart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCart holder, int position) {

        ModelCart modelCart = cartList.get(position);
        final String title = modelCart.getTitle();
        String category = modelCart.getPrductCategory();
        String itemImage = modelCart.getProfileImage();
        String quantity = modelCart.getQuantity();
        String actualPrice = modelCart.getActualPrice();
        String finalPrice = modelCart.getFinalPrice();
        String discountPercent = modelCart.getDiscountPercent();
        final String productId = modelCart.getProductId();
        final String shopId =  modelCart.getShopId();

        holder.titleTv.setText(title);
        holder.categoryTv.setText(category);
        holder.orignalPriceTv.setText(actualPrice);
        holder.discountPriceTv.setText(finalPrice);
        holder.quantityTv.setText(quantity);
        holder.discountPercentTv.setText(discountPercent);
        if (modelCart.getDiscountAvailable().equals("true")){
            holder.discountPercentTv.setVisibility(View.VISIBLE);
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.orignalPriceTv.setPaintFlags(holder.orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.discountPercentTv.setVisibility(View.GONE);
            holder.discountPriceTv.setVisibility(View.GONE);
            holder.orignalPriceTv.setTextColor(Color.BLACK);
            holder.orignalPriceTv.setTextSize(20);
        }
        try {
            Picasso.get().load(itemImage).placeholder(R.drawable.ic_cart_primary).into(holder.productIconIv);
        }
        catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.ic_cart_primary);
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE")
                        .setMessage("Sure want to delete product " + title + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(productId,shopId);
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
    }

    private void deleteProduct(String id, String shopId){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).child("CartItem").child(shopId).child(id).removeValue()
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
        return cartList.size();
    }

    class HolderCart extends RecyclerView.ViewHolder{

        private ImageView productIconIv;
        private TextView titleTv, categoryTv, discountPriceTv, orignalPriceTv, discountPercentTv, quantityTv;
        private ImageButton deleteBtn;

        public HolderCart(@NonNull View itemView) {
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIV);
            titleTv = itemView.findViewById(R.id.titleTV);
            categoryTv = itemView.findViewById(R.id.categoryTV);
            discountPriceTv = itemView.findViewById(R.id.discountPriceTV);
            orignalPriceTv = itemView.findViewById(R.id.orignalPriceTV);
            discountPercentTv = itemView.findViewById(R.id.discountPercentTV);
            quantityTv = itemView.findViewById(R.id.quantityTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
