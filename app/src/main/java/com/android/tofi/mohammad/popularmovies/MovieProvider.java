package com.android.tofi.mohammad.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by mohammad tofi on 6.11.2015.
 */
public class MovieProvider extends ContentProvider {
    MovieDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int MOVIE = 1;
    static final int REVIEW_WITH_MOVIE_ID = 10;
    static final int VIDEO_WITH_MOVIE_ID = 20;
    static final int UPDATE_MOVIE_BY_ID = 30;
    static final int REVIEW = 2;
    static final int VIDEO = 3;

    private static final SQLiteQueryBuilder sMovieWithVideoIdQueryBuilder;
    private static final SQLiteQueryBuilder sReviewByMovieIdQueryBuilder;

    static {
        sMovieWithVideoIdQueryBuilder = new SQLiteQueryBuilder();
        sReviewByMovieIdQueryBuilder = new SQLiteQueryBuilder();


        sMovieWithVideoIdQueryBuilder.setTables(
                MovieContract.VideoEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.VideoEntry.TABLE_NAME +
                        "." + MovieContract.VideoEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID
        );

        sReviewByMovieIdQueryBuilder.setTables(
                MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID
        );
    }

    private static final String sMovieId =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";


    private Cursor getVideoByMovieId(Uri uri, String[] projection, String sortOrder) {
        String idMovie = MovieContract.MovieEntry.getPosterUrlFromUri(uri);
        return sMovieWithVideoIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieId,
                new String[]{idMovie},
                null,
                null,
                null
        );
    }
///    android.database.sqlite.SQLiteException: ambiguous column name:
// id: , while compiling: SELECT review._id, review.movie_id, author, id,
// content FROM review INNER JOIN movie ON review.movie_id = movie.id WHERE
// (movie.id = ? )

    private Cursor getReviewByMovieId(Uri uri, String[] projection, String sortOrder) {
        String idMovie = MovieContract.MovieEntry.getPosterUrlFromUri(uri);
        return sReviewByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieId,
                new String[]{idMovie},
                null,
                null,
                null
        );
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case VIDEO:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEW:
                db.beginTransaction();
                returnCount = 0;
                int valuesLength = values.length;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VIDEO_WITH_MOVIE_ID:
                retCursor = getVideoByMovieId(uri, projection, sortOrder);
                break;
            case REVIEW_WITH_MOVIE_ID:
                retCursor = getReviewByMovieId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case REVIEW_WITH_MOVIE_ID:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case VIDEO_WITH_MOVIE_ID:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown Uri" + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case VIDEO: {
                long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO:
                rowsDeleted = db.delete(
                        MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdate;
        switch (match) {
            case MOVIE:
                rowsUpdate = db.update(
                        MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEW:
                rowsUpdate = db.update(
                        MovieContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEO:
                rowsUpdate = db.update(
                        MovieContract.VideoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdate != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdate;

    }

    //content://com.android.tofi.mohammad.popularmovies/review/131634
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/#", VIDEO_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", UPDATE_MOVIE_BY_ID);
        return matcher;
    }

    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
