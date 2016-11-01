package com.android.tofi.mohammad.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hamzaK on 5.11.2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_RATE + " REAL NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED + " INTEGER DEFAULT 0,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE + " Text NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE + " INTEGER NOT NULL"
                + " );";

        final String SQL_CREATE_REVIEW_TABLE = " CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " ( "
                + MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY,"
                + MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT + " TEXT,"
                + MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR + " TEXT,"
                + MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_ID + " TEXT UNIQUE NOT NULL,"
                + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY ( " + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " ) REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ) );";

        final String SQL_CREATE_VIDEO_TABLE = " CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + " ( "
                + MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_KEY + " TEXT NOT NULL,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_LANGUAGE + " TEXT ,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SITE + " TEXT ,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SIZE + " TEXT ,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TITLE + " TEXT NOT NULL,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TYPE + " TEXT ,"
                + MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_ID + " TEXT UNIQUE NOT NULL,"
                + " FOREIGN KEY (" + MovieContract.VideoEntry.COLUMN_MOVIE_ID + " ) REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ) );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
