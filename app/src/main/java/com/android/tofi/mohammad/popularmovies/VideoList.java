package com.android.tofi.mohammad.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hamzaK on 13.10.2015.
 */
public class VideoList {
    @SerializedName("results")
    private ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
}
