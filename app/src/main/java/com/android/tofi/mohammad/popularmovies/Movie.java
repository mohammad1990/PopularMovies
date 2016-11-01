package com.android.tofi.mohammad.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohammad tofi on 17.8.2015.
 */


public class Movie implements Parcelable {

    public Movie() {
    }

    @SerializedName("original_title")
    private String movieTitle;
    @SerializedName("vote_average")
    private double movieVoteAverage;
    @SerializedName("overview")
    private String movieOverview;
    @SerializedName("release_date")
    private String movieReleaseDate;
    @SerializedName("poster_path")
    private String poster_path_movie;
    @SerializedName("id")
    private int movieId;
    @SerializedName("vote_count")
    private int movieVoteCount;

    private int favouriteMovie;

    private int typeMovie;

    public int getTypeMovie() {
        return typeMovie;
    }

    public void setTypeMovie(int typeMovie) {
        this.typeMovie = typeMovie;
    }

    public int getFavouriteMovie() {
        return favouriteMovie;
    }

    public void setFavouriteMovie(int favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public double getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(double movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getPoster_path_movie() {
        return poster_path_movie;
    }

    public void setPoster_path_movie(String movieBackdropPath) {
        this.poster_path_movie = movieBackdropPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieVoteCount() {
        return movieVoteCount;
    }

    public void setMovieVoteCount(int movieVoteCount) {
        this.movieVoteCount = movieVoteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeDouble(movieVoteAverage);
        dest.writeString(movieOverview);
        dest.writeString(movieReleaseDate);
        dest.writeString(poster_path_movie);
        dest.writeInt(movieId);
        dest.writeInt(movieVoteCount);
        dest.writeInt(favouriteMovie);
        dest.writeInt(typeMovie);

    }

    public Movie(String movieTitle, Double movieVoteAverage, String movieOverview, String movieReleaseDate, String poster_path_movie, int movieId, int movieVoteCount, int favouriteMovie, int typeMovie) {
        this.movieTitle = movieTitle;
        this.movieVoteAverage = movieVoteAverage;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.poster_path_movie = poster_path_movie;
        this.movieId = movieId;
        this.movieVoteCount = movieVoteCount;
        this.favouriteMovie = favouriteMovie;
        this.typeMovie = typeMovie;

    }

    private Movie(Parcel in) {
        this.movieTitle = in.readString();
        this.movieVoteAverage = in.readDouble();
        this.movieOverview = in.readString();
        this.movieReleaseDate = in.readString();
        this.poster_path_movie = in.readString();
        this.movieId = in.readInt();
        this.movieVoteCount = in.readInt();
        this.favouriteMovie = in.readInt();
        this.typeMovie = in.readInt();

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
