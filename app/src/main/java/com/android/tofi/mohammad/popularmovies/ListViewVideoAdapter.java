package com.android.tofi.mohammad.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by hamzaK on 13.10.2015.
 */
public class ListViewVideoAdapter extends CursorAdapter {
    private Context mContext;
    public ListViewVideoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder  holder = (ViewHolder) view.getTag();
        String colVideoName = cursor.getString(DetailFragment.COLUMN_MOVIE_VIDEO_TITLE);
        holder.textview.setText(colVideoName);
        holder.imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.video_play_button));
    }

    class ViewHolder {
        TextView textview;
        ImageView imageView;
        public ViewHolder(View root)
        {
            textview = (TextView) root.findViewById(R.id.video_title);
            imageView = (ImageView) root.findViewById(R.id.video_thumbnail);
        }
    }
}


