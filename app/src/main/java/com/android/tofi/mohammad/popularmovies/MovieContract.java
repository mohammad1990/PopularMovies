package com.android.tofi.mohammad.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohammad tofi on 4.11.2015.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.android.tofi.mohammad.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "video";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_MOVIE_DATE_RELEASE = "release_date";
        public static final String COLUMN_MOVIE_RATE = "vote_average";
        public static final String COLUMN_MOVIE_SUMMARY = "location";
        public static final String COLUMN_MOVIE_IMAGE = "poster_path";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_FAVORED = "favored";
        public static final String COLUMN_MOVIE_TYPE_MOVIE="type_movie";
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Long getIdMovieFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String getPosterUrlFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_REVIEW_AUTHOR = "author";
        public static final String COLUMN_MOVIE_REVIEW_CONTENT = "content";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*   public static Uri buildReviewWithId(int idMovie) {
               return CONTENT_URI.buildUpon().appendPath(PATH_REVIEW)
                       .appendQueryParameter(COLUMN_MOVIE_ID, Integer.toString(idMovie)).build();
           }*/
        public static Uri buildReviewWithId(int movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }
    }

    public static final class VideoEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_VIDEO_TITLE = "name";
        public static final String COLUMN_MOVIE_VIDEO_KEY = "key";
        public static final String COLUMN_MOVIE_VIDEO_LANGUAGE = "iso_639_1";
        public static final String COLUMN_MOVIE_VIDEO_SITE = "site";
        public static final String COLUMN_MOVIE_VIDEO_SIZE = "size";
        public static final String COLUMN_MOVIE_VIDEO_TYPE = "type";
        public static final String COLUMN_MOVIE_VIDEO_ID = "id";

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

   /*     public static Uri buildVideoWithId(int idMovie) {
            return CONTENT_URI.buildUpon().appendPath(PATH_VIDEO)
                    .appendQueryParameter(COLUMN_MOVIE_ID, Integer.toString(idMovie)).build();
        }*/
        public static Uri buildVideoWithId(int movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

    }
}
