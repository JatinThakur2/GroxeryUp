package com.example.groceryapp;

import android.widget.Filter;

import com.example.groceryapp.adapters.AdapterProductBuyer;
import com.example.groceryapp.adapters.AdapterProductSeller;
import com.example.groceryapp.models.ModelProduct;

import java.util.ArrayList;

public class FilterProductBuyer extends Filter {

    private AdapterProductBuyer adapter;
    private ArrayList<ModelProduct> filterList;

    public FilterProductBuyer(AdapterProductBuyer adapter, ArrayList<ModelProduct> filterList){
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<ModelProduct> filterModels = new ArrayList<>();
            for (int i =0; i<filterList.size(); i++){
                if (filterList.get(i).getPrductTitle().toUpperCase().contains(constraint)||
                    filterList.get(i).getPrductCategory().toUpperCase().contains(constraint)){
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.productList = (ArrayList<ModelProduct>) results.values;
        adapter.notifyDataSetChanged();
    }
}
