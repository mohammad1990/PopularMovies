package com.android.tofi.mohammad.popularmovies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamzaK on 13.10.2015.
 */
public class Video {
    private String id;
    @SerializedName("iso_639_1")
    private String language;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public Video(String id, String language, String key, String name, String site, String size, String type) {
        this.id = id;
        this.language = language;
        this.key = key;
        this.name = name;
        this.site = site;
        this.site = size;
        this.type = type;
    }

    public Video(String key) {
        this.key = key;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
