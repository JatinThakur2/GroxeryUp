package com.example.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.AdapterOrderedItem;
import com.example.groceryapp.models.ModelOrderedItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderDetailsBuyerActivity extends AppCompatActivity {

    private String orderFrom, orderId, deliveryFee;

    private ImageButton backBtn, writeReviewBtn;
    private TextView orderIdTv, dateTv, orderStatusTv, shopNameTv, totalIemTv, amountTv, addressTv;
    private RecyclerView itemsRv;

    private FirebaseAuth mAuth;

    private ArrayList<ModelOrderedItems> orderedItemsArrayList;
    private AdapterOrderedItem adapterOrderedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_buyer);

        backBtn = findViewById(R.id.backBtn);
        orderIdTv = findViewById(R.id.orderIdTV);
        dateTv = findViewById(R.id.dateTV);
        orderStatusTv = findViewById(R.id.orderStatusTV);
        shopNameTv = findViewById(R.id.shopNameTV);
        totalIemTv = findViewById(R.id.totalItemTV);
        amountTv = findViewById(R.id.amountTV);
        addressTv = findViewById(R.id.deliveryAddressTV);
        itemsRv = findViewById(R.id.itemsRV);
        writeReviewBtn = findViewById(R.id.writeReviewBtn);

        final Intent intent = getIntent();
        orderFrom = intent.getStringExtra("orderFrom");
        orderId = intent.getStringExtra("orderId");

        mAuth = FirebaseAuth.getInstance();
        loadShopInfo();
        loadOrdersDetails();
        loadOrderItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderDetailsBuyerActivity.this, WriteReviewActivity.class);
                intent1.putExtra("shopUid", orderFrom);
                startActivity(intent1);
            }
        });

    }

    String totalItems;
    private void loadOrderItems() {
        orderedItemsArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(orderFrom).child("Orders").child(orderId).child("items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderedItemsArrayList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelOrderedItems modelOrderedItems = ds.getValue(ModelOrderedItems.class);
                            orderedItemsArrayList.add(modelOrderedItems);
                        }
                        adapterOrderedItem = new AdapterOrderedItem(OrderDetailsBuyerActivity.this, orderedItemsArrayList);
                        itemsRv.setAdapter(adapterOrderedItem);
                        totalItems = ""+dataSnapshot.getChildrenCount();
                        totalIemTv.setText(totalItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadOrdersDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(orderFrom).child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String orderBy = ""+dataSnapshot.child("orderBy").getValue();
                        String orderCost = ""+dataSnapshot.child("orderCost").getValue();
                        String orderId = ""+dataSnapshot.child("orderId").getValue();
                        String orderStatus = ""+dataSnapshot.child("orderStatus").getValue();
                        String orderTime = ""+dataSnapshot.child("orderTime").getValue();
                        String orderFrom = ""+dataSnapshot.child("orderFrom").getValue();
                        String latitude = ""+dataSnapshot.child("latitude").getValue();
                        String longitude = ""+dataSnapshot.child("longitude").getValue();

                        orderCost = orderCost.replaceAll("\\(.*[\\)]", "").replaceAll(" \\(", "").replaceAll("\\)", "");

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(orderTime));
                        String formateDate = DateFormat.format("dd/MM/yyyy hh:mm a", calendar).toString();

                        if (orderStatus.equals("In Progress")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else if (orderStatus.equals("Completed")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                        }
                        else {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed01));
                        }

                        orderIdTv.setText(orderId);
                        orderStatusTv.setText(orderStatus);
                        double total = Double.parseDouble(orderCost)+Double.parseDouble(deliveryFee);
                        amountTv.setText("$"+ total +" [including $"+ deliveryFee +" Delivery Fee]");
                        dateTv.setText(formateDate);

                        findAddress(latitude, longitude);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void findAddress(String latitude, String longitude) {
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            addressTv.setText(address + ", " + city + ", " + state + ", " + country);
        }
        catch (Exception e) {

        }
    }

    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(orderFrom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String shopName = ""+dataSnapshot.child("shopName").getValue();
                        deliveryFee = ""+dataSnapshot.child("deliveryFee").getValue();
                        shopNameTv.setText(shopName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
