package com.android.tofi.mohammad.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by mohammad tofi on 3.8.2015.
 */
public class DetailActivity extends ActionBarActivity {
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        movie = getIntent().getParcelableExtra(getResources().getString(R.string.intent_movie_name));
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.intent_movie_name), movie);
        DetailFragment sendToFragment = new DetailFragment();
        sendToFragment.setArguments(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_shared_derail);
        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (movie != null) {
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getMovieTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getMovieReleaseDate() + "\n" + "\n" + movie.getMovieOverview() + "\n" + movie.getMovieVoteCount());
            startActivity(Intent.createChooser(
                    shareIntent,
                    getResources().getString(R.string.share_movie)));
        }
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        if (id == R.id.action_shared_derail) {
            createShareIntent();
        }
        return super.onOptionsItemSelected(item);
    }


}