package com.movie_management_system.movie_details.Controller;

import com.movie_management_system.movie_details.Entity.Movie;
import com.movie_management_system.movie_details.Repository.MovieDao;
import com.movie_management_system.movie_details.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDao movieDao;


    @PostMapping(value = "/movies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> addingNewMovie(@RequestPart("movieData") Movie movie, @RequestPart(value = "image")
            MultipartFile image, @RequestPart(value = "trailer") MultipartFile trailer)
    {
        try {

            String imgPath = movieService.uploadFile(image);
            movie.setImagePath(imgPath);

            String trailerPath = movieService.uploadFile(trailer);
            movie.setTrailerPath(trailerPath);

            String res = movieService.addNewMovie(movie);
            if (res != null)
                return ResponseEntity.ok("Movie Successfully saved in DB");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to save movie in DB");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred:" + e.getMessage());
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovieImages() {

        return ResponseEntity.ok(movieService.retrieverAll());
    }

}
