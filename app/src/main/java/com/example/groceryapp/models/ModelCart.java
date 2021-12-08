package com.example.groceryapp.models;

public class ModelCart {
    private String actualPrice, discountAvailable, discountPercent, finalPrice, prductCategory,
            profileImage, quantity, title, uid, productId, ShopId;

    public ModelCart() {
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPrductCategory() {
        return prductCategory;
    }

    public void setPrductCategory(String prductCategory) {
        this.prductCategory = prductCategory;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ModelCart(String actualPrice, String discountAvailable, String discountPercent, String finalPrice, String prductCategory,
                     String profileImage, String quantity, String title, String uid, String productId, String shopId) {
        this.actualPrice = actualPrice;
        this.discountAvailable = discountAvailable;
        this.discountPercent = discountPercent;
        this.finalPrice = finalPrice;
        this.prductCategory = prductCategory;
        this.profileImage = profileImage;
        this.quantity = quantity;
        this.title = title;
        this.uid = uid;
        this.productId = productId;
        this.ShopId = shopId;
    }
}
