package com.android.tofi.mohammad.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamzaK on 11.9.2015.
 */

public class MovieList {
    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @SerializedName("results")
    private  ArrayList <Movie> movies;
}
