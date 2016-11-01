package com.android.tofi.mohammad.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.android.tofi.mohammad.popularmovies.sync.PopularMovieAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamzaK on 26.11.2015.
 */
public class Utility {
    public static void storeMovie(Context context, Movie movie, int typeMovie) {
        ContentValues coValues = new ContentValues();
        //  List<String> favouriteMovieList = getFavouriteMovie(context);
        //get the rating
        Double voteAverage = movie.getMovieVoteAverage();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATE, voteAverage);
        //get the movie data from the JSON response
        //get the title
        String title = movie.getMovieTitle();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);

        //get the movie release date
        String releaseDate = movie.getMovieReleaseDate();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE, releaseDate);

        //get the description of the movie
        String description = movie.getMovieOverview();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY, description);

        int movieId = movie.getMovieId();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

        //get the poster url
        String posterPath = movie.getPoster_path_movie();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, posterPath);


        int favouriteMovie = movie.getFavouriteMovie();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED, favouriteMovie);

        //get the total number of votes
        int totalVotes = movie.getMovieVoteCount();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, totalVotes);

        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE, typeMovie);

        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, coValues);
    }

    public static void storeMovieList(Context context, List<Movie> movieList, int typeMovie) {
        ArrayList<ContentValues> cvList = new ArrayList<>();
        List<String> favouriteMovieList = getFavouriteMovie(context);
        if (context != null) {
            for (int i = 0; i < movieList.size(); i++) {
                Movie movie = movieList.get(i);
                ContentValues coValues = new ContentValues();

                //get the rating
                Double voteAverage = movie.getMovieVoteAverage();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATE, voteAverage);
                //get the movie data from the JSON response
                //get the title
                String title = movie.getMovieTitle();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);

                //get the movie release date
                String releaseDate = movie.getMovieReleaseDate();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE, releaseDate);

                //get the description of the movie
                String description = movie.getMovieOverview();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY, description);

                int movieId = movie.getMovieId();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

                //get the poster url
                String posterPath = movie.getPoster_path_movie();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, posterPath);


                int favouriteMovie = movie.getFavouriteMovie();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED, favouriteMovie);

                //get the total number of votes
                int totalVotes = movie.getMovieVoteCount();
                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, totalVotes);

                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE, typeMovie);
                if (!favouriteMovieList.contains(Integer.toString(movieId))) {
                    cvList.add(coValues);
                }
            }
            //insert into the DB
            ContentValues[] values = new ContentValues[cvList.size()];
            cvList.toArray(values);

            int itemsAdded = context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, values);

        }
    }

    public static int checkMovie(Context context, int movieId) {
        if (context != null) {
            Cursor c = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movieId)}, null);
            if (c != null) {
                //if (c.moveToFirst()) {
                if (c.getCount() == 0) {
                    return 0;
                } else
                    return c.getCount();
            }
        }
        return 0;
    }

    public static void updateMovie(Context context, Movie movie, int typeMovie) {
        ContentValues coValues = new ContentValues();
        //  List<String> favouriteMovieList = getFavouriteMovie(context);
        //get the rating
        Double voteAverage = movie.getMovieVoteAverage();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATE, voteAverage);
        //get the movie data from the JSON response
        //get the title
        String title = movie.getMovieTitle();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);

        //get the movie release date
        String releaseDate = movie.getMovieReleaseDate();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE, releaseDate);

        //get the description of the movie
        String description = movie.getMovieOverview();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY, description);

        int movieId = movie.getMovieId();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

        //get the poster url
        String posterPath = movie.getPoster_path_movie();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, posterPath);


        /*int favouriteMovie = movie.getFavouriteMovie();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED, favouriteMovie);*/

        //get the total number of votes
        int totalVotes = movie.getMovieVoteCount();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, totalVotes);

        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE, typeMovie);

        context.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI,
                coValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getMovieId())});
    }

    public static List<String> getFavouriteMovie(Context context) {
        List<String> favouriteMovie = new ArrayList<>();
        if (context != null) {
            Cursor c = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                    null, null,
                    null);
            if (c.moveToFirst()) {
                do {
                    int s = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                    favouriteMovie.add(c.getString(s));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favouriteMovie;
    }

    public static ContentValues updateFavouriteMovie(Context context, Movie movie) {
        ContentValues coValues = new ContentValues();
        int favouriteMovie = movie.getFavouriteMovie();
        coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED, favouriteMovie);
        return coValues;
    }

    public static int getCountOfTable(Context context, int typeMovie) {
        if (context != null) {
            Cursor c = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE + " = ?" + " AND " + MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED + " = ?", new String[]{String.valueOf(typeMovie), String.valueOf(0)}, null);
            if (c != null) {
                //if (c.moveToFirst()) {
                if (c.getCount() == 0) {
                    return 0;
                } else {
                    if (c.getCount() > 0)
                        return c.getCount();
                }
            }
            //   }
        }
        return -1;
    }

    public static Cursor getVideosMovie(Context context, int idMovie) {
        Cursor c = context.getContentResolver().query(MovieContract.VideoEntry.buildVideoWithId(idMovie),
                null,
                null, null,
                null);
        if (c != null) {
            if (c.getCount() == 0) {
                return null;
            } else {
                if (c.getCount() > 0)
                    return c;
            }
        }
        return null;
    }

    public static Cursor findMovieInVideo(Context context, int idMovie) {
        Cursor c = null;
        if (context != null) {
            c = context.getContentResolver().query(MovieContract.VideoEntry.buildVideoWithId(idMovie),
                    null,
                    null, null,
                    null);
        }
        return c;
    }

    public static Cursor findMovieInReview(Context context, int idMovie) {
        Cursor c = null;
        if (context != null) {
            c = context.getContentResolver().query(MovieContract.ReviewEntry.buildReviewWithId(idMovie),
                    null,
                    null, null,
                    null);
        }
        return c;
    }

    public static void updateMovieReview(Context context, int idMovie, List<Review> reviewsList) {
        for (int i = 0; i < reviewsList.size(); i++) {
            Review review = reviewsList.get(i);
            ContentValues values = new ContentValues();
            String reviewID = review.getId();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_ID, reviewID);
            String reviewAuthor = review.getAuthor();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR, reviewAuthor);
            String reviewContent = review.getContent();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT, reviewContent);

            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, idMovie);
            context.getContentResolver().update(MovieContract.ReviewEntry.CONTENT_URI,
                    values, MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_ID + " = ?",
                    new String[]{String.valueOf(reviewID)});
        }


    }

    public static void updateMovieVideo(Context context, int idMovie, List<Video> videoList) {
        ContentValues values = new ContentValues();
        if (context != null) {
            for (int i = 0; i < videoList.size(); i++) {
                Video video = videoList.get(i);

                String videoID = video.getId();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_ID, videoID);

                String videoKey = video.getKey();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_KEY, videoKey);

                String videoLanguage = video.getLanguage();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_LANGUAGE, videoLanguage);

                String videoName = video.getName();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TITLE, videoName);

                String videoSite = video.getSite();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SITE, videoSite);

                String videoSize = video.getSize();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SIZE, videoSize);

                String videoType = video.getType();
                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TYPE, videoType);

                values.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, idMovie);

                context.getContentResolver().update(MovieContract.VideoEntry.CONTENT_URI,
                        values, MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_ID + " = ?",
                        new String[]{String.valueOf(videoID)});
            }
        }
    }

    public static void storeReviewMovie(Context context, int idMovie, List<Review> reviewsList) {
        ArrayList<ContentValues> crValue = new ArrayList<ContentValues>();
        for (int i = 0; i < reviewsList.size(); i++) {
            Review review = reviewsList.get(i);
            ContentValues values = new ContentValues();
            String reviewID = review.getId();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_ID, reviewID);
            String reviewAuthor = review.getAuthor();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR, reviewAuthor);
            String reviewContent = review.getContent();
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT, reviewContent);
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, idMovie);

            crValue.add(values);
        }
        ContentValues[] values = new ContentValues[crValue.size()];
        crValue.toArray(values);
        int itemsAdded = context.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, values);
    }

    public static void storeVideoMovie(Context context, int idMovie, List<Video> videoList) {
        ArrayList<ContentValues> crValue = new ArrayList<ContentValues>();
        for (int i = 0; i < videoList.size(); i++) {
            Video video = videoList.get(i);

            ContentValues values = new ContentValues();
            String videoID = video.getId();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_ID, videoID);

            String videoKey = video.getKey();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_KEY, videoKey);

            String videoLanguage = video.getLanguage();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_LANGUAGE, videoLanguage);

            String videoName = video.getName();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TITLE, videoName);

            String videoSite = video.getSite();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SITE, videoSite);

            String videoSize = video.getSize();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SIZE, videoSize);

            String videoType = video.getType();
            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TYPE, videoType);

            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, idMovie);

            crValue.add(values);
        }
        ContentValues[] values = new ContentValues[crValue.size()];
        crValue.toArray(values);
        if (values != null) {
            int itemsAdded = context.getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, values);
        }
    }

    /* public static void getMovieIdFromUri(Context context, Uri movieUri) {
         long _id = MovieContract.MovieEntry.getIdMovieFromUri(movieUri);
         Cursor c = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                 new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                 MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(_id)},
                 null);
     }*/
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
    }

    @SuppressWarnings("ResourceType")
    static public @PopularMovieAdapter.LocationStatus int getLocationState(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getInt(c.getString(R.string.pref_location_status_key), PopularMovieAdapter.LOCATION_STATUS_OK);
    }


}
