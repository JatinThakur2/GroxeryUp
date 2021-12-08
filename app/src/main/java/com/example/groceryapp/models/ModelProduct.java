package com.example.groceryapp.models;

public class ModelProduct {
    private String prductId, prductTitle, prductDescription, prductCategory, prductQuantity, prductIcon,
            orignalPrice, discountPrice, discountPercent, discountAvailable, timestamp, uid;

    public ModelProduct() {
    }

    public String getPrductId() {
        return prductId;
    }

    public void setPrductId(String prductId) {
        this.prductId = prductId;
    }

    public String getPrductTitle() {
        return prductTitle;
    }

    public void setPrductTitle(String prductTitle) {
        this.prductTitle = prductTitle;
    }

    public String getPrductDescription() {
        return prductDescription;
    }

    public void setPrductDescription(String prductDescription) {
        this.prductDescription = prductDescription;
    }

    public String getPrductCategory() {
        return prductCategory;
    }

    public void setPrductCategory(String prductCategory) {
        this.prductCategory = prductCategory;
    }

    public String getPrductQuantity() {
        return prductQuantity;
    }

    public void setPrductQuantity(String prductQuantity) {
        this.prductQuantity = prductQuantity;
    }

    public String getPrductIcon() {
        return prductIcon;
    }

    public void setPrductIcon(String prductIcon) {
        this.prductIcon = prductIcon;
    }

    public String getOrignalPrice() {
        return orignalPrice;
    }

    public void setOrignalPrice(String orignalPrice) {
        this.orignalPrice = orignalPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ModelProduct(String prductId, String prductTitle, String prductDescription, String prductCategory,
                        String prductQuantity, String prductIcon, String orignalPrice, String discountPrice,
                        String discountPercent, String discountAvailable, String timestamp, String uid) {
        this.prductId = prductId;
        this.prductTitle = prductTitle;
        this.prductDescription = prductDescription;
        this.prductCategory = prductCategory;
        this.prductQuantity = prductQuantity;
        this.prductIcon = prductIcon;
        this.orignalPrice = orignalPrice;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.discountAvailable = discountAvailable;
        this.timestamp = timestamp;
        this.uid = uid;


    }
}
