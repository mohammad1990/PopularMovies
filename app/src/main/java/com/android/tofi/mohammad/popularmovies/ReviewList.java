package com.android.tofi.mohammad.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hamzaK on 11.10.2015.
 */
public class ReviewList {

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @SerializedName("results")
    private ArrayList<Review> reviews;
}
