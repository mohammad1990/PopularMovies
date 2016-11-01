package com.android.tofi.mohammad.popularmovies;

import android.app.Application;
import android.content.Context;

/**
 * Created by hamzaK on 4.9.2015.
 */
public class MyApplication extends Application {
    String orderingMovie;
    private static Context context;
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public String getOrderingMovie() {
        return orderingMovie;
    }

    public void setOrderingMovie(String orderingMovie) {
         this.orderingMovie = orderingMovie;
    }


    public static Boolean getStateOfArrayList() {
        return stateOfArrayList;
    }

    public static void setStateOfArrayList(Boolean stateOfArrayList) {
        MyApplication.stateOfArrayList = stateOfArrayList;
    }

    public static  Boolean stateOfArrayList =true;

}
