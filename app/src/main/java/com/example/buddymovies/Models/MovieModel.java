package com.example.buddymovies.Models;

import java.io.Serializable;
import java.net.URL;

public class MovieModel implements Serializable {
    private final String title;
    private final String releaseYear;
    private final String genre;
    private final URL imgSrc;
    private final int id;
    private String description;

    public MovieModel(String title, String releaseYear, String genre, URL imgSrc, int id) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.imgSrc = imgSrc;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() { return genre; }

    public URL getImgSrc() {
        return imgSrc;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
