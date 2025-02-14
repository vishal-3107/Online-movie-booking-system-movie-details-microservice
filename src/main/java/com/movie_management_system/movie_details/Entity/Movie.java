package com.movie_management_system.movie_details.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Entity
@Table(name="movies")
@Component
public class Movie {

    @Id
    private String movieName;

    //@Column(unique = true, nullable = false)
    private String movieDetails;

    @NotNull
    @Column(nullable = false)
    private Integer movieRating;

    @NotNull
    @Column(name = "image_path")
    private String imagePath;

    @NotNull
    private String trailerPath;

    public Movie(){}

    public Movie(String movieName, String movieDetails, Integer movieRating, String imagePath, String trailerPath) {
        this.movieName = movieName;
        this.movieDetails = movieDetails;
        this.movieRating = movieRating;
        this.imagePath = imagePath;
        this.trailerPath = trailerPath;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDetails() {
        return movieDetails;
    }

    public void setMovieDetails(String movieDetails) {
        this.movieDetails = movieDetails;
    }

    public @NotNull Integer getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(@NotNull Integer movieRating) {
        this.movieRating = movieRating;
    }

    public @NotNull String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@NotNull String imagePath) {
        this.imagePath = imagePath;
    }

    public @NotNull String getTrailerPath() {
        return trailerPath;
    }

    public void setTrailerPath(@NotNull String trailerPath) {
        this.trailerPath = trailerPath;
    }
}
