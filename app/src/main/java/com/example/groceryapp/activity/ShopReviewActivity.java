package com.example.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.AdapterReview;
import com.example.groceryapp.models.ModelReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShopReviewActivity extends AppCompatActivity {

    private String shopId;
    private ArrayList<ModelReview> reviewArrayList;
    private AdapterReview adapterReview;

    FirebaseAuth mAuth;

    private ImageButton backBtn;
    private ImageView profileIv;
    private TextView shopNameTv, ratingTv;
    private RatingBar ratingBar;
    private RecyclerView reviewRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_review);

        shopId = getIntent().getStringExtra("shopId");

        mAuth = FirebaseAuth.getInstance();

        loadShopDetails();
        loadReviews();

        backBtn = findViewById(R.id.backBtn);
        profileIv = findViewById(R.id.profileIV);
        shopNameTv = findViewById(R.id.shopNameTV);
        ratingBar = findViewById(R.id.ratingBar);
        ratingTv = findViewById(R.id.ratingTV);
        reviewRv = findViewById(R.id.reviewRV);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private float ratingSum = 0;
    private void loadReviews() {
        reviewArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopId).child("Ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reviewArrayList.clear();
                        ratingSum = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            float rating = Float.parseFloat(""+ds.child("ratings").getValue());
                            ratingSum = ratingSum+rating;

                            ModelReview modelReview = ds.getValue(ModelReview.class);
                            reviewArrayList.add(modelReview);
                        }

                        adapterReview = new AdapterReview(ShopReviewActivity.this, reviewArrayList);
                        reviewRv.setAdapter(adapterReview);

                        long numberOfReview = dataSnapshot.getChildrenCount();
                        float avgRating = ratingSum/numberOfReview;

                        ratingTv.setText(String.format("%.2f", avgRating) + "[" + numberOfReview + "]");
                        ratingBar.setRating(avgRating);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopId)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String shopName = ""+dataSnapshot.child("shopName").getValue();
                String profileImage = ""+dataSnapshot.child("profileImage").getValue();

                shopNameTv.setText(shopName);
                try {
                    Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_gray).into(profileIv);
                }
                catch (Exception e) {
                    profileIv.setImageResource(R.drawable.ic_store_gray);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
