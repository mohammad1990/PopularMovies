package com.android.tofi.mohammad.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by mohammad tofi on 3.8.2015.
 */
public class GridViewAdapter extends CursorAdapter {
    private Context mcontext;
 /*   private int layoutResourceId;
    private ArrayList data = new ArrayList();*/

    public GridViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mcontext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        String colOriginalTitle = cursor.getString(MainFragment.COL_ORIGINAL_TITLE);
        String colImagePostLink = cursor.getString(MainFragment.COLUMN_MOVIE_IMAGE);
        holder.textview.setText(colOriginalTitle);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w500/" + colImagePostLink)
                .resize(300, 400)
                .centerCrop()
                .noFade()
                .error(R.drawable.abc_list_selector_holo_dark)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.image.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.image.setVisibility(View.INVISIBLE);
                    }
                });

    }


    /*public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }*/

    class ViewHolder {
        public final ImageView image;
        public final TextView textview;
        public final ProgressBar progressBar;

        public ViewHolder(View root) {
            image = (ImageView) root.findViewById(R.id.imageView_movie_grid_view);
            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            textview = (TextView) root.findViewById(R.id.text);
        }
    }

}


