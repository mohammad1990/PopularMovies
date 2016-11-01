package com.android.tofi.mohammad.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by hamzaK on 24.1.2016.
 */
public class PopularMovieAuthenticatorService extends Service {
    private PopularMovieAuthenticator mAuthenticator;


    @Override
    public void onCreate() {
        mAuthenticator = new PopularMovieAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
