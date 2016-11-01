package com.android.tofi.mohammad.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hamzaK on 12.10.2015.
 */
public class ListViewReviewAdapter extends CursorAdapter {
    private Context mContext;

    public ListViewReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String colContent = cursor.getString(DetailFragment.COLUMN_MOVIE_REVIEW_CONTENT);
        String colAuthor = cursor.getString(DetailFragment.COLUMN_MOVIE_REVIEW_AUTHOR);
        holder.textViewFirst.setText(colAuthor);
        holder.textViewSecond.setText(colContent);
    }

    class ViewHolder {
        TextView textViewFirst;
        TextView textViewSecond;

        public ViewHolder(View root) {
            textViewFirst = (TextView) root.findViewById(R.id.title_review);
            textViewSecond = (TextView) root.findViewById(R.id.content_review);
        }
    }

}

