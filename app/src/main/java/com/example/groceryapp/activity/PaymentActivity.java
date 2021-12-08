package com.example.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    private TextInputEditText upiEt, nameEt, noteEt;
    private Button payBtn;
    private TextView amountTv;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    private String shopId, myLatitude, myLongitude, grandTotal;

    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amountTv = findViewById(R.id.amountTV);
        upiEt = findViewById(R.id.upiET);
        nameEt = findViewById(R.id.upiET);
        noteEt = findViewById(R.id.noteET);
        payBtn = findViewById(R.id.payBtn);

        shopId = getIntent().getStringExtra("ShopId");
        myLatitude = getIntent().getStringExtra("Latitude");
        myLongitude = getIntent().getStringExtra("Longitude");
        grandTotal = getIntent().getStringExtra("GrandTotal");
        grandTotal = grandTotal.replace("$", "").replaceAll("\\(.*?\\)", "").trim();
        amountTv.setText(grandTotal);
        Log.d("Amount", amountTv.getText()+"");

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountTv.getText().toString();
                String upiId = upiEt.getText().toString();
                String name = nameEt.getText().toString();
                String note = noteEt.getText().toString();
                payUsingUpi(amount, upiId, name, note);
            }
        });
    }

    private void payUsingUpi(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent intent = Intent.createChooser(upiPayIntent, "Pay With");

        if (null != intent.resolveActivity(getPackageManager())) {
            startActivityForResult(intent, UPI_PAYMENT);
        }
        else {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String text = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + text);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(text);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                submitOrder();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void submitOrder() {
        mProgressDialog.setMessage("Placing Order....");
        mProgressDialog.show();

        final String timestamp = ""+System.currentTimeMillis();
        String cost = grandTotal;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", timestamp);
        hashMap.put("orderTime", timestamp);
        hashMap.put("orderStatus", "In Progress");
        hashMap.put("orderCost", cost);
        hashMap.put("orderBy", ""+mAuth.getUid());
        hashMap.put("OrderFrom", ""+shopId);
        hashMap.put("latitude", myLatitude);
        hashMap.put("longitude", myLongitude);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(shopId).child("Orders");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
                        ref1.child("CartItem").child(shopId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    String finalPrice =""+ds.child("finalPrice").getValue();
                                    String productCategory =""+ds.child("prductCategory").getValue();
                                    String ItemImage = ""+ds.child("profileImage").getValue();
                                    String quantity = ""+ds.child("quantity").getValue();
                                    String title = ""+ds.child("title").getValue();
                                    String pId = ""+ds.child("productId").getValue();

                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                    hashMap1.put("finalPrice", finalPrice);
                                    hashMap1.put("productCategory", productCategory);
                                    hashMap1.put("ItemImage", ItemImage);
                                    hashMap1.put("quantity", quantity);
                                    hashMap1.put("title", title);
                                    hashMap1.put("pId", pId);

                                    Log.d("title", title);

                                    ref.child(timestamp).child("items").child(pId).setValue(hashMap1);
                                }
                                mProgressDialog.dismiss();
                                Toast.makeText(PaymentActivity.this, "Order Placed Successfully...", Toast.LENGTH_SHORT).show();

//                                Intent intent = new Intent(CartActivity.this, OrderDetailsBuyerActivity.class);
//                                intent.putExtra("orderFrom", shopId);
//                                intent.putExtra("orderId", timestamp);
//                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        ref1.child("CartItem").removeValue().equals(shopId);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(PaymentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
