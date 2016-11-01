package com.android.tofi.mohammad.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by hamzaK on 24.1.2016.
 */
public class PopularMovieSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PopularMovieAdapter sSunshineSyncAdapter = null;


    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new PopularMovieAdapter(getApplicationContext(), true);
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}
