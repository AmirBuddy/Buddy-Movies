package com.example.buddymovies.Models;

import java.util.ArrayList;

public class ContainerModel {
    private final String title;
    private final ArrayList<MovieModel> movieList;

    public ContainerModel(String title, ArrayList<MovieModel> movieList) {
        this.title = title;
        this.movieList = movieList;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<MovieModel> getMovieList() {
        return movieList;
    }
}
