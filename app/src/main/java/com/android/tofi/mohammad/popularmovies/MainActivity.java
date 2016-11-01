package com.android.tofi.mohammad.popularmovies;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by mohammad tofi on 3.8.2015.
 */
public class MainActivity extends ActionBarActivity implements OnItemPressListener {
    DetailFragment f2;
    FragmentManager manager;
    MenuItem menuItem;
    ShareActionProvider shareActionProvider;
    Movie movies;

    // Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
    }

    @Override
    protected void onDestroy() {
 /*       Cursor c = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                MainFragment.MOVIE_COLUMNS,
                MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED + " = ?",
                new String[]{String.valueOf(0)}, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                getContentResolver().delete(MovieContract.VideoEntry.CONTENT_URI, MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{c.getString(MainFragment.COL_MOVIE_ID)});
                getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI, MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(c.getString(MainFragment.COL_MOVIE_ID))});
            }*/
        // }
       /* getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED + " = ?",
                new String[]{String.valueOf(0)});*/
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            menuItem = menu.findItem(R.id.action_shared);
        } else {
            menu.getItem(0).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent(Movie movie) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (movies != null) {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getMovieTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getMovieReleaseDate() + "\n" + "\n" + movie.getMovieOverview());
            startActivity(Intent.createChooser(
                    shareIntent,
                    getResources().getString(R.string.share_movie)));
        }
        return shareIntent;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        if (id == R.id.action_shared) {
            createShareIntent(movies);
        }
        return super.onOptionsItemSelected(item);
    }


    //for call fragment and send movie from activity to fragment
    @Override
    public void onItemPressed(Movie movie) {
        movies = movie;
        f2 = (DetailFragment) manager.findFragmentById(R.id.fragment_detail);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            if (f2 != null && f2.isVisible()) {
                f2.setMovie(movie);
                f2.putValuesMovieInDetailFragment(movie);
            }
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(getResources().getString(R.string.intent_movie_name), movie);
            startActivity(intent);

        }

    }

}
