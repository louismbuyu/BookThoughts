package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

public class ResponseItunesBook {

    String trackName;
    String artworkUrl100;
    String releaseDate;
    String description;
    String artistName;

    public ResponseItunesBook(String trackName, String artworkUrl100, String releaseDate, String description, String artistName) {
        this.trackName = trackName;
        this.artworkUrl100 = artworkUrl100;
        this.releaseDate = releaseDate;
        this.description = description;
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
