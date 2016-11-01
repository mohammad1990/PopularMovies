package com.android.tofi.mohammad.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Mohammad tofi on 12.10.2015.
 */
/*implements LoaderManager.LoaderCallbacks<Cursor>*/
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //View in Detail Activity
    TextView titleMovie;
    RatingBar ratingBar;
    ImageView imageView;
    TextView overViewMovie;
    TextView releaseDateMovie;
    ListView listReview;
    ListView listVideo;
    CheckBox favoriteMovies;
    TextView textViewLabel;
    TextView textViewLabelReview;
    TextView listViewLabel;
    //list Reviews Movie
    ListViewReviewAdapter listAdapterReview = null;

    public final static String urlVideoTrial= "http://www.youtube.com/watch?v=";

    public  final static String urlImageMovie="http://image.tmdb.org/t/p/w150/";
    ////list TrailVideo Movie
    ListViewVideoAdapter listAdapterVideo = null;
    //Movie object I put the movie which come from main activity after user was clicked.
    private Movie movie;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    //This list have list from all video.
  //  ArrayList<Video> videos;

    //This Failed I use in swatch.
    private static final int MOVIE_VIDEO_LOADER = 1;
    private static final int MOVIE_REVIEW_LOADER = 2;

    //all video columns for use in query.
    public static final String[] MOVIE_VIDEO_COLUMNS = {
            MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID,
            MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry.COLUMN_MOVIE_ID,
            MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TITLE,
            // MovieContract.VideoEntry.COLUMN_MOVIE_ID,
            MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_TYPE,
            MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_KEY,
            MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SITE,
            MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_SIZE
    };
    public static final int COLUMN_MOVIE_Video_PK_ID = 0;
    public static final int COLUMN_Video_ID = 1;
    public static final int COLUMN_MOVIE_VIDEO_TITLE = 2;
    public static final int COLUMN_MOVIE_VIDEO_TYPE = 3;
    public static final int COLUMN_MOVIE_VIDEO_KEY = 4;
    public static final int COLUMN_MOVIE_VIDEO_LANGUAGE = 5;
    public static final int COLUMN_MOVIE_VIDEO_SITE = 6;
    public static final int COLUMN_MOVIE_VIDEO_SIZE = 7;

    //all review columns for use in query.
    private static final String[] MOVIE_REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT,
    };
    public static final int COLUMN_MOVIE_REVIEW_PK_ID = 0;
    public static final int COLUMN_REVIEW_MOVIE_ID = 1;
    public static final int COLUMN_MOVIE_REVIEW_AUTHOR = 2;
    public static final int COLUMN_MOVIE_REVIEW_ID = 3;
    public static final int COLUMN_MOVIE_REVIEW_CONTENT = 4;

    private ArrayList convertCursorToArrayList(Cursor cursor) {
         ArrayList<Video> listVideoItem = new ArrayList<Video>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                listVideoItem.add(new Video(cursor.getString(cursor.getColumnIndex(MovieContract.VideoEntry.COLUMN_MOVIE_VIDEO_KEY))));
            }
        }
        return listVideoItem;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        titleMovie = (TextView) root.findViewById(R.id.titleDetialMovie);
        ratingBar = (RatingBar) root.findViewById(R.id.ratingMovie);
        imageView = (ImageView) root.findViewById(R.id.postImageMovie);
        overViewMovie = (TextView) root.findViewById(R.id.overviewMovie);
        listReview = (ListView) root.findViewById(R.id.list_review);
        listVideo = (ListView) root.findViewById(R.id.list_video);
        releaseDateMovie = (TextView) root.findViewById(R.id.dateReleaseMovie);
        textViewLabel = (TextView) root.findViewById(R.id.TextViewText);
        textViewLabelReview = (TextView) root.findViewById(R.id.text_view_label_review);
        listViewLabel = (TextView) root.findViewById(R.id.list_view_label);
        favoriteMovies = (CheckBox) root.findViewById(R.id.favorite_movies);
        favoriteMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteMovies.isChecked()) {
                    addFavouriteMovieToDatabase(true);
                } else if (!favoriteMovies.isChecked())
                    addFavouriteMovieToDatabase(false);
            }
        });
        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Cursor c= Utility.getVideosMovie(getActivity(), movie.getMovieId());
                if (c != null) {
                ArrayList<Video> f= convertCursorToArrayList(c);
                if(f!=null)
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(urlVideoTrial + f.get(position).getKey())));
                }
            }
        });
        //set all value movie in Views.
        overViewMovie.setMovementMethod(new ScrollingMovementMethod());
        movie = getActivity().getIntent().getParcelableExtra(getResources().getString(R.string.intent_movie_name));
        if (movie != null) {
            putValuesMovieInDetailFragment(movie);
          //  getReviewMovie();
            listAdapterVideo = new ListViewVideoAdapter(getActivity(), null, 0);
            listVideo.setAdapter(listAdapterVideo);
            listAdapterReview = new ListViewReviewAdapter(getActivity(), null, 0);
            listReview.setAdapter(listAdapterReview);
        } else {
            favoriteMovies.setVisibility(View.INVISIBLE);
            titleMovie.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            overViewMovie.setVisibility(View.INVISIBLE);
            listReview.setVisibility(View.INVISIBLE);
            releaseDateMovie.setVisibility(View.INVISIBLE);
            textViewLabel.setText("");
            textViewLabelReview.setText("");
            listViewLabel.setText("");

        }
        return root;
    }

    public void putValuesMovieInDetailFragment(Movie movie) {
        favoriteMovies.setVisibility(View.VISIBLE);
        titleMovie.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        overViewMovie.setVisibility(View.VISIBLE);
        listReview.setVisibility(View.VISIBLE);
        releaseDateMovie.setVisibility(View.VISIBLE);
        if (movie.getMovieOverview() != null)
            textViewLabel.setText(getResources().getString(R.string.summary_movie));
        textViewLabelReview.setText(getResources().getString(R.string.review_movie));
        listViewLabel.setText(getResources().getString(R.string.video_trial_movie));
        titleMovie.setText(movie.getMovieTitle());
        Double voteAverage = new Double(movie.getMovieVoteAverage());
        ratingBar.setRating(voteAverage.floatValue());
        overViewMovie.setText(movie.getMovieOverview());
        releaseDateMovie.setText(movie.getMovieReleaseDate());
        if (movie.getFavouriteMovie() == 0) {
            favoriteMovies.setChecked(false);
        } else {
            favoriteMovies.setChecked(true);
        }
        if (movie.getPoster_path_movie() != null) {
            Picasso.with(getActivity().getApplicationContext())
                    .load(urlImageMovie + movie.getPoster_path_movie())
                    .resize(Integer.parseInt(getResources().getString(R.string.width_image)),Integer.parseInt(getResources().getString(R.string.height_image)))
                    .centerCrop()
                    .noFade()
                    .into(imageView);
        }

        //get list review movies from the api themoviedb then store result in the database
       // getReviewMovie();
        //get list video movies from the api themoviedb then store result in the database
        //getVideoTrail();
        getLoaderManager().restartLoader(1, null, this);
        getLoaderManager().restartLoader(2, null, this);
        listAdapterReview = new ListViewReviewAdapter(getActivity(), null, 0);
        listReview.setAdapter(listAdapterReview);
        listAdapterVideo = new ListViewVideoAdapter(getActivity(), null, 0);
        listVideo.setAdapter(listAdapterVideo);
    }


    //here update favourite field in database.
    private void addFavouriteMovieToDatabase(boolean movieState) {
        if (movieState) {
            movie.setFavouriteMovie(1);
            ContentValues value = Utility.updateFavouriteMovie(getActivity(), movie);
            getActivity().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI,
                    value, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(movie.getMovieId())});
        } else if (!movieState) {
            movie.setFavouriteMovie(0);
            ContentValues value = Utility.updateFavouriteMovie(getActivity(), movie);
            getActivity().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI,
                    value, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(movie.getMovieId())});
        }
    }

    //I use this method for initLoader.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*private void getVideoTrail() {
        String apiKey = "e75ad8d476b669e408b2e04f7b8ac60a";
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://api.themoviedb.org")
                .build();
        GetMovieApi git = restAdapter.create(GetMovieApi.class);
        git.getVideo(movie.getMovieId() + "", apiKey, new Callback<VideoList>() {

            @Override
            public void success(VideoList videoList, Response response) {
                Cursor c = Utility.findMovieInVideo(getActivity(), movie.getMovieId());
                if (c != null) {
                    if (!c.moveToFirst())
                        if (videoList != null && c.getCount() == 0) {
                            Utility.storeVideoMovie(getActivity(), movie.getMovieId(), videoList.getVideos());
                            videos = videoList.getVideos();
                        }
                } else {
                    if (c != null) {
                        Utility.updateMovieVideo(getActivity(), movie.getMovieId(), videoList.getVideos());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getActivity(), "this operation is field", Toast.LENGTH_LONG).show();

            }
        });

    }*/

    /*private void getReviewMovie() {
        String apiKey = "e75ad8d476b669e408b2e04f7b8ac60a";
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://api.themoviedb.org")
                .build();
        GetMovieApi git = restAdapter.create(GetMovieApi.class);*/
        /*git.getReview(movie.getMovieId() + "", apiKey, new Callback<ReviewList>() {

                    @Override
                    public void success(ReviewList reviewList, Response response) {
                        Cursor c = Utility.findMovieInReview(getActivity(), movie.getMovieId());
                        if (c != null) {
                            if (!c.moveToFirst())
                                if (reviewList != null && c.getCount() == 0) {
                                    Utility.storeReviewMovie(getActivity(), movie.getMovieId(), reviewList.getReviews());
                                }
                        } else {
                            if (c != null)
                                Utility.updateMovieReview(getActivity(), movie.getMovieId(), reviewList.getReviews());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //       Toast.makeText(getActivity(), "this operation is field", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_VIDEO_LOADER: {
                Loader<Cursor> videoList = new CursorLoader(getActivity(),
                        MovieContract.VideoEntry.buildVideoWithId(movie.getMovieId()),
                        MOVIE_VIDEO_COLUMNS, MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(movie.getMovieId())},
                        null);
                return videoList;
            }
            case MOVIE_REVIEW_LOADER: {
                Loader<Cursor> reviewList = new CursorLoader(getActivity(),
                        MovieContract.ReviewEntry.buildReviewWithId(movie.getMovieId())
                        , MOVIE_REVIEW_COLUMNS, MovieContract.MovieEntry._ID + " = ?"
                        , new String[]{String.valueOf(movie.getMovieId())}
                        , null);
                return reviewList;
            }
            default:
                Toast.makeText(getActivity(), "This movie don't have any trial video or review", Toast.LENGTH_LONG).show();
                return null;
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //listAdapterVideo = new ListViewVideoAdapter(getActivity(), null, 0);
        switch (loader.getId()) {
            case MOVIE_VIDEO_LOADER: {
                if (data.getCount() < 0 || data != null)
                    listAdapterVideo.swapCursor(data);
                break;
            }
            case MOVIE_REVIEW_LOADER: {
                // if (data.getCount() > 0 || !(data.moveToFirst()))
                if (data.getCount() < 0 || data != null)
                    listAdapterReview.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case MOVIE_VIDEO_LOADER: {
                listAdapterVideo.swapCursor(null);
            }
            case MOVIE_REVIEW_LOADER: {
                listAdapterReview.swapCursor(null);
            }

        }
    }

}
