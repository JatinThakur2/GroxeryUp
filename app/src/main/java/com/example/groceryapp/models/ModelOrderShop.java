package com.example.groceryapp.models;

public class ModelOrderShop {
    private String orderId,  orderTime, orderStatus, orderCost, orderBy, OrderFrom, latitude, longitude;

    public ModelOrderShop() {
    }

    public ModelOrderShop(String orderId, String orderTime, String orderStatus, String orderCost, String orderBy, String orderFrom, String latitude, String longitude) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderCost = orderCost;
        this.orderBy = orderBy;
        OrderFrom = orderFrom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getOrderFrom() {
        return OrderFrom;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
