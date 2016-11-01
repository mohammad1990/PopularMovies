package com.android.tofi.mohammad.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.android.tofi.mohammad.popularmovies.GetMovieApi;
import com.android.tofi.mohammad.popularmovies.MainActivity;
import com.android.tofi.mohammad.popularmovies.Movie;
import com.android.tofi.mohammad.popularmovies.MovieList;
import com.android.tofi.mohammad.popularmovies.R;
import com.android.tofi.mohammad.popularmovies.ReviewList;
import com.android.tofi.mohammad.popularmovies.Utility;
import com.android.tofi.mohammad.popularmovies.VideoList;

import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hamzaK on 24.1.2016.
 */
public class PopularMovieAdapter extends AbstractThreadedSyncAdapter {
    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;

    public PopularMovieAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    private static long lastSyncTime = 0L;
    private static final int MOVIE_NOTIFICATION_ID = 1001;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    int typeMovie;

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        String apiKey = "e75ad8d476b669e408b2e04f7b8ac60a";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String urlRequest = pref.getString(getContext().getString(R.string.pref_movie_key), "Most Popular");

        if (urlRequest != null && urlRequest.equals("Most Popular"))
            typeMovie = 3;
        else {
            if (urlRequest != null && urlRequest.equals("Highest Rated"))
                typeMovie = 4;
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://api.themoviedb.org")
                .build();
        GetMovieApi git = restAdapter.create(GetMovieApi.class);
        if (urlRequest.equals("Most Popular")) {
            getData(git, apiKey, "popular", typeMovie);
        } else {
            getData(git, apiKey, "top_rated", typeMovie);
        }

        //  GetMovieApi git = restAdapter.create(GetMovieApi.class);


    }

    static private void setLocationStatus(Context context, int locationStatus) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(context.getString(R.string.pref_location_status_key), locationStatus);
        spe.commit();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID, LOCATION_STATUS_UNKNOWN})
    public @interface LocationStatus {
    }

    public void getData(final GetMovieApi get, final String apiKey, String orderingType, final int typeMovie) {
//here I make request to video
        get.getMovie(orderingType, apiKey, new Callback<MovieList>() {
            @Override
            public void success(MovieList movies, Response response) {
                final List<Movie> moviesWithOutNull = deleteEmptyItem(movies.getMovies());
                if (moviesWithOutNull != null) {
                    // Utility.storeMovieList(getContext(), moviesWithOutNull, typeMovie);
                    //Start Fetch the video
                    for (final Movie movie : moviesWithOutNull) {
                        if (Utility.checkMovie(getContext(), movie.getMovieId()) == 0) {
                            Utility.storeMovie(getContext(), movie, typeMovie);
                        } else {
                            Utility.updateMovie(getContext(), movie, typeMovie);
                        }
//here I make request to video API
                        get.getVideo(movie.getMovieId() + "", apiKey, new Callback<VideoList>() {
                            @Override
                            public void success(VideoList videoList, Response response) {
                                Cursor c = Utility.findMovieInVideo(getContext(), movie.getMovieId());
                                if (c != null) {
                                    if (!c.moveToFirst())
                                        if (videoList != null && c.getCount() == 0) {
                                            Utility.storeVideoMovie(getContext(), movie.getMovieId(), videoList.getVideos());
                                        }
                                } else {
                                    if (c != null) {
                                        Utility.updateMovieVideo(getContext(), movie.getMovieId(), videoList.getVideos());
                                    }
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                setLocationStatus(getContext(),LOCATION_STATUS_SERVER_INVALID);
                            }
                        });
//here I make request to API review
                        get.getReview(movie.getMovieId() + "", apiKey, new Callback<ReviewList>() {
                            @Override
                            public void success(ReviewList reviewList, Response response) {
                                Cursor c = Utility.findMovieInReview(getContext(), movie.getMovieId());
                                if (c != null) {
                                    if (!c.moveToFirst())
                                        if (reviewList != null && c.getCount() == 0) {
                                            Utility.storeReviewMovie(getContext(), movie.getMovieId(), reviewList.getReviews());
                                        }
                                } else {
                                    if (c != null)
                                        Utility.updateMovieReview(getContext(), movie.getMovieId(), reviewList.getReviews());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                    setLocationStatus(getContext(),LOCATION_STATUS_SERVER_INVALID);                            }
                        });


                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //setLocationStatus(getContext(),LOCATION_STATUS_SERVER_INVALID);
            }
        });
        sendNotification();
    }

    //Delete empty item from movies arrayList
    public ArrayList deleteEmptyItem(ArrayList movies) {
        for (int x = movies.size() - 1; x > 0; x--) {
            Movie movie = (Movie) movies.get(x);
            if (movie.getPoster_path_movie() == null)
                movies.remove(x);
        }
        return movies;
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);


        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));


        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {


        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */


        }
        return newAccount;
    }

    private void sendNotification() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean displayNotifications = prefs.getBoolean(getContext().getString(R.string.Snotification_enable), true);
        if (!displayNotifications) {
            return;
        }
        String lastNotificationKey = getContext().getString(R.string.prefs_notification_last_key);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            //Show notification

            int smallIcon = R.mipmap.ic_launcher;
            Bitmap largeIcon = BitmapFactory.decodeResource(
                    getContext().getResources(),
                    R.mipmap.ic_launcher);

            android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(smallIcon)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getContext().getString(R.string.app_name))
                    .setContentText(getContext().getString(R.string.notification_content));

            Intent notificationIntent = new Intent(getContext(), MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(MOVIE_NOTIFICATION_ID, builder.build()); //notify

            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastNotificationKey, System.currentTimeMillis());
            editor.apply();
        }
    }

}
