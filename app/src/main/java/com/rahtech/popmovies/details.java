package com.rahtech.popmovies;

import java.util.ArrayList;

public class details {
    String title="";
    int id;
    int vote_average;
    String poster_path="";
    String backdrop_path="";
    ArrayList<Integer> genre_ids=null;
    String overview="";
    String release_date="";
    boolean adult;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isAdult() {
        return adult;
    }

    public details(String title, int id, int vote_average, String poster_path, String backdrop_path, ArrayList<Integer> genre_ids, String overview, String release_date, boolean adult) {
        this.title = title;
        this.id = id;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genre_ids;
        this.overview = overview;
        this.release_date = release_date;
        this.adult = adult;
    }
}
