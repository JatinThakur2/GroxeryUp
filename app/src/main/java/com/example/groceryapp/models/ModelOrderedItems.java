package com.example.groceryapp.models;

public class ModelOrderedItems {
    private String pId, title, quantity, ItemImage, productCategory, finalPrice;

    public ModelOrderedItems() {
    }

    public ModelOrderedItems(String pId, String title, String quantity, String itemImage, String productCategory, String finalPrice) {
        this.pId = pId;
        this.title = title;
        this.quantity = quantity;
        ItemImage = itemImage;
        this.productCategory = productCategory;
        this.finalPrice = finalPrice;
    }

    public String getpId() {
        return pId;
    }

    public String getTitle() {
        return title;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getFinalPrice() {
        return finalPrice;
    }
}
