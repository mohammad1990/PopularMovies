package com.android.tofi.mohammad.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.tofi.mohammad.popularmovies.sync.PopularMovieAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a movies view.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,SharedPreferences.OnSharedPreferenceChangeListener {
    //adapter for fill all movies in main activity.
    private GridViewAdapter gridAdapter;
    //list from movies.
    private ArrayList<Movie> listMovieItem = new ArrayList<Movie>();
    //I use for return to same place in gridAdapter in main activity movies.
    private int index;
    //gridView which I put all movies inside it.
    private GridView gridView;

    View emptyView;
    //listener for I can movie field from main activity to the detail Fragment.
    OnItemPressListener itemListener;
    // that field for I can choose the type of the movies I need "most_popular" or "highest_rated"
    String urlRequest = "Most Popular";
    int typeMovie;
    //All the columns movies in the database.
    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE,
            MovieContract.MovieEntry.COLUMN_MOVIE_RATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY,
            MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE
    };
    public static final int COL_MOVIE_PK_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_ORIGINAL_TITLE = 2;
    public static final int COLUMN_MOVIE_DATE_RELEASE = 3;
    public static final int COLUMN_MOVIE_RATE = 4;
    public static final int COLUMN_MOVIE_SUMMARY = 5;
    public static final int COLUMN_MOVIE_IMAGE = 6;
    public static final int COLUMN_MOVIE_FAVORED = 7;
    public static final int COLUMN_MOVIE_VOTE_COUNT = 8;
    public static final int COLUMN_MOVIE_TYPE_MOVIE = 9;


    @Override
    public void onStart() {

        super.onStart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        urlRequest = pref.getString(getString(R.string.pref_movie_key),getResources().getString(R.string.url_request_most_popular));
        if (((MyApplication) getActivity().getApplication()).getOrderingMovie() == null) {
            ((MyApplication) getActivity().getApplication()).setOrderingMovie(getResources().getString(R.string.url_request_highest_rated));
            typeMovie = 4;
            // enter = false;
        }
        if (urlRequest.equals(getResources().getString(R.string.url_request_favoured_movie))) {
            getLoaderManager().destroyLoader(-1);
            getLoaderManager().restartLoader(1, null, this);

        } else {
            if (urlRequest != null && urlRequest.equals(getResources().getString(R.string.url_request_most_popular))) {
                typeMovie = 3;
            } else {
                if (urlRequest != null && urlRequest.equals(getResources().getString(R.string.url_request_highest_rated))) {
                    typeMovie = 4;
                }
            }
            if (urlRequest.equals(((MyApplication) getActivity().getApplication()).getOrderingMovie()) == false && urlRequest.equals(getResources().getString(R.string.url_request_favoured_movie)) == false) {
                updateOrderingMovie();
            } else {
                getLoaderManager().destroyLoader(1);
                getLoaderManager().restartLoader(-1, null, this);
            }
            if (!listMovieItem.isEmpty()) {
                MyApplication.setStateOfArrayList(false);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        gridAdapter = new GridViewAdapter(getActivity(), null, 0);
        gridView = (GridView) root.findViewById(R.id.gridview_movie);
        View emptyView = root.findViewById(R.id.list_view_empty_main_activity);
        gridView.setEmptyView(emptyView);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = gridView.getFirstVisiblePosition();
                Movie movie = listMovieItem.get(position);
                itemListener.onItemPressed(movie);
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainfragment, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            itemListener = (OnItemPressListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    //here I save position grid view for return
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        index = gridView.getFirstVisiblePosition();
        outState.putInt(getResources().getString(R.string.position_grid_view), index);
        ((MyApplication) getActivity().getApplication()).setOrderingMovie(urlRequest);
        outState.putInt(getResources().getString(R.string.sitting_user_choose), typeMovie);
    }

    ///This method I make initLoader.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //this method I return the value position the user before the click.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(getResources().getString(R.string.position_grid_view));
            typeMovie = savedInstanceState.getInt(getResources().getString(R.string.sitting_user_choose));
        } else {
            index = 0;
        }
    }

    //here I select between two request that related to setting
    public void updateOrderingMovie() {
        PopularMovieAdapter.syncImmediately(getActivity());
        getLoaderManager().destroyLoader(1);
        getLoaderManager().restartLoader(-1, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //this method make Refresh activity when internet is coming
 /*   public static void Refresh(Context ctx) {
        if (MyApplication.isActivityVisible() == true && MyApplication.getStateOfArrayList()) {
            Intent i = new Intent(ctx, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == -1) {
            Loader<Cursor> cursorLoader = new CursorLoader(getActivity(),
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE + " = ?",
                    new String[]{String.valueOf(typeMovie)},
                    null);
            return cursorLoader;
        } else if (id == 1) {
            Loader<Cursor> cursorLoader = new CursorLoader(getActivity(),
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED + " = ?",
                    new String[]{String.valueOf(1)},
                    null);
            return cursorLoader;
        }
        return null;
    }

    private void convertCursorToArrayList(Cursor cursor) {
        listMovieItem = new ArrayList<Movie>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                listMovieItem.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)),
                                cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATE)),
                                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY)),
                                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE_RELEASE)),
                                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE)),
                                cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                                cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT)),
                                cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED)),
                                cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE)))
                );
            }
            if (listMovieItem.isEmpty() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Movie movie = listMovieItem.get(0);
                itemListener.onItemPressed(movie);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        convertCursorToArrayList(cursor);
        gridAdapter.swapCursor(cursor);
        if (!listMovieItem.isEmpty()) {
            MyApplication.setStateOfArrayList(false);
        } else {
            MyApplication.setStateOfArrayList(true);

        }
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        gridAdapter.swapCursor(null);
    }

    private void updateEmptyView() {
        if (gridView.getCount() == 0) {
            TextView tv = (TextView) getView().findViewById(R.id.list_view_empty_main_activity);

            if (tv != null) {
                @PopularMovieAdapter.LocationStatus int state=Utility.getLocationState(getActivity());
                int message = R.string.empty_main_activity;
                switch (state)
                {
                    case PopularMovieAdapter.LOCATION_STATUS_SERVER_DOWN:
                    {
                        message=R.string.empty_main_activity_location_status_server_down;
                        break;
                    }
                    case PopularMovieAdapter.LOCATION_STATUS_SERVER_INVALID:
                        message=R.string.empty_main_activity_location_status_server_invalid;
                        break;
                    case PopularMovieAdapter.LOCATION_STATUS_UNKNOWN:
                        message=R.string.empty_main_activity_location_status_server_unknown;
                        break;
                    default:
                        if (!Utility.isNetworkAvailable(getActivity())) {
                            message = R.string.main_activity_no_network;
                        }
                }
                tv.setText(message);
            }
        }
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }
    /* public static class InternetReceiver extends BroadcastReceiver {
        public void notificationStateInternet() {
            Refresh(MyApplication.getAppContext());
        }

        //check state internet
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeNetInfoMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean isConnected = (activeNetInfoWifi != null && activeNetInfoWifi.isConnectedOrConnecting()) || (activeNetInfoMobile != null && activeNetInfoMobile.isConnectedOrConnecting());
            Bundle bundle = new Bundle();
            bundle.putSerializable("MyData", true);
            if (isConnected) {
                notificationStateInternet();
                Log.i("NET", "connect" + isConnected);
            } else {

                Log.i("NET", "not connect" + isConnected);
            }
        }
    }*/

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ( key.equals(getString(R.string.pref_location_status_key)) ) {
            updateEmptyView();
        }

    }
}
