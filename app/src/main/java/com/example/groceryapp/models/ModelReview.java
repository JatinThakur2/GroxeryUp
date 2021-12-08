package com.example.groceryapp.models;

public class ModelReview {

    String uid, ratings, review, timestamp;

    public ModelReview() {
    }

    public ModelReview(String uid, String ratings, String review, String timestamp) {
        this.uid = uid;
        this.ratings = ratings;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getRatings() {
        return ratings;
    }

    public String getReview() {
        return review;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
