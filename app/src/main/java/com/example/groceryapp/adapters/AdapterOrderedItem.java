package com.example.groceryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.models.ModelOrderedItems;

import java.util.ArrayList;

public class AdapterOrderedItem extends RecyclerView.Adapter<AdapterOrderedItem.HolderOrderedItem> {

    private Context context;
    private ArrayList<ModelOrderedItems> orderedItemsArrayList;

    public AdapterOrderedItem(Context context, ArrayList<ModelOrderedItems> modelOrderedItems) {
        this.context = context;
        this.orderedItemsArrayList = modelOrderedItems;
    }

    @NonNull
    @Override
    public HolderOrderedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ordered_items, parent, false);
        return new HolderOrderedItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderedItem holder, int position) {
        ModelOrderedItems modelOrderedItems = orderedItemsArrayList.get(position);
        String getpId = modelOrderedItems.getpId();
        String title = modelOrderedItems.getTitle();
        String cost = modelOrderedItems.getFinalPrice();
        String quantity = modelOrderedItems.getQuantity();
        String category = modelOrderedItems.getProductCategory();
        String itemImage = modelOrderedItems.getItemImage();

        holder.itemTitleTv.setText(title);
        holder.itemCategoryTv.setText(category);
        holder.priceEachTv.setText(cost);
        holder.itemQuantityTv.setText("["+quantity+"]");
    }

    @Override
    public int getItemCount() {
        return orderedItemsArrayList.size();
    }

    class HolderOrderedItem extends RecyclerView.ViewHolder{

        private TextView itemTitleTv, itemCategoryTv, priceEachTv, itemQuantityTv;

        public HolderOrderedItem(@NonNull View itemView) {
            super(itemView);

            itemTitleTv = itemView.findViewById(R.id.itemTitleTV);
            itemCategoryTv = itemView.findViewById(R.id.itemcategoryTV);
            priceEachTv = itemView.findViewById(R.id.priceEachTV);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTV);
        }
    }
}
