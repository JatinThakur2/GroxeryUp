package com.example.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.AdapterCart;
import com.example.groceryapp.adapters.AdapterProductBuyer;
import com.example.groceryapp.models.ModelCart;
import com.example.groceryapp.models.ModelProduct;
import com.example.groceryapp.models.ModelShop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private Button placeOrder;
    private RecyclerView cartItemRv;
    private TextView grandTotal, cartIsEmpty, detailCost, detailDelivery, detailTotal;
    private TableLayout tableLayout;

    private String myLatitude, myLongitude, myPhone, shopId, deliveryFee;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    private ArrayList<ModelCart> cartProductList;
    private AdapterCart mAdapterCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        shopId = getIntent().getStringExtra("ShopUid");

        backBtn = findViewById(R.id.backBtn);
        cartItemRv = findViewById(R.id.cartItemRV);
        grandTotal = findViewById(R.id.grandTotalTV);
        placeOrder = findViewById(R.id.placeOrder);
        cartIsEmpty = findViewById(R.id.cartIsEmpty);
        detailCost = findViewById(R.id.detailCost);
        detailDelivery = findViewById(R.id.detailDelivery);
        detailTotal = findViewById(R.id.detailTotal);
        tableLayout = findViewById(R.id.tableLayout);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadCartItems();
        loadMyInfo();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLatitude.equals("") || myLatitude.equals("null") || myLongitude.equals("") || myLongitude.equals("null")){
                    Toast.makeText(CartActivity.this, "Please enter your address in your profile before placing order", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (myPhone.equals("") || myPhone.equals("null")){
                    Toast.makeText(CartActivity.this, "Please enter your phone number in your profile before placing order", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("ShopId", shopId);
                intent.putExtra("Latitude", myLatitude);
                intent.putExtra("Longitude", myLongitude);
//                int upiTotal = Integer.parseInt(grandTotal.getText().toString())+Integer.valueOf(deliveryFee);
//                Log.d("upiTotal", upiTotal+"");
                intent.putExtra("GrandTotal", grandTotal.getText().toString().trim());
                startActivity(intent);
            }
        });


    }


    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            myPhone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                            String deliveryFee = ""+ds.child("deliveryFee").getValue();
                            myLatitude = ""+ds.child("latitude").getValue();
                            myLongitude = ""+ds.child("longitude").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadCartItems() {

        cartProductList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).child("CartItem").child(shopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        cartProductList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelCart modelCart = ds.getValue(ModelCart.class);
                            cartProductList.add(modelCart);
                        }
                        mAdapterCart = new AdapterCart(CartActivity.this, cartProductList);
                        cartItemRv.setAdapter(mAdapterCart);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        ref.child(mAuth.getUid()).child("CartItem").child(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    placeOrder.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.VISIBLE);
                    cartIsEmpty.setVisibility(View.GONE);
                    grandTotalPrice((Map<String, Object>) dataSnapshot.getValue());
                } else {
                    grandTotal.setText("$0");
                    cartIsEmpty.setVisibility(View.VISIBLE);
                    placeOrder.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void grandTotalPrice(Map<String, Object> items) {
        ArrayList<String> total = new ArrayList<>();
        Double sum = 0.0;
        final Double[] detailTotals = {0.0};

        for (Map.Entry<String, Object> entry : items.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            total.add((String) singleUser.get("finalPrice"));
        }
        for (int i = 0; i < total.size(); i++) {
            total.set(i, total.get(i).replace("$", ""));
            sum += Double.parseDouble(total.get(i));
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        final Double finalSum = sum;
        ref.child(shopId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryFee = ""+dataSnapshot.child("deliveryFee").getValue();
                Log.d("delivery fee", deliveryFee);
                detailDelivery.setText("$"+deliveryFee);
                detailTotals[0] = finalSum + Double.parseDouble(deliveryFee);
                detailTotal.setText(String.valueOf(detailTotals[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        detailCost.setText("$"+sum);
        grandTotal.setText("$"+String.valueOf(sum)+ " "+"(" + total.size()+")");
    }
}
