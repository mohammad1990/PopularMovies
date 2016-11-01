package com.android.tofi.mohammad.popularmovies;

import com.android.tofi.mohammad.popularmovies.Movie;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mohammad tofi on 7.9.2015.
 */
public interface GetMovieApi {
    @GET("/3/movie/{movie}")
    public void getMovie(@Path("movie") String typeMovie,@Query("api_key") String keyApi, Callback<MovieList> response);
    @GET("/3/movie/{idMovie}/reviews")
    public  void getReview(@Path("idMovie") String idMovie,@Query("api_key") String keyApi, Callback<ReviewList> response );
    @GET("/3/movie/{idMovie}/videos")
    public  void getVideo(@Path("idMovie") String idMovie,@Query("api_key") String keyApi, Callback<VideoList> response );



    //response is the response from the server which is now in the POJO
}
