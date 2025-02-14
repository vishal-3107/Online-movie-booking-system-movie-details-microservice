package com.movie_management_system.movie_details.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.movie_management_system.movie_details.Entity.Movie;
import com.movie_management_system.movie_details.Repository.MovieDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MovieService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private MovieDao movieDao;

    public String addNewMovie(Movie movie) {
        movieDao.save(movie);
        return "Data Successfully saved in DB";
    }


    public String uploadFile(MultipartFile file) {
        File fileObj = converMultiPartFileToFile(file);
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
        fileObj.delete();
        return "https://" + bucketName + ".s3." + ".amazonaws.com/" + filename;
    }

    private File converMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }

        return convertedFile;
    }

    public String getPreSignedUrl(String imageKey) {
        if (imageKey == null || imageKey.isEmpty()) {
            return null; // Handle null or empty input safely
        }

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, imageKey, HttpMethod.GET);
        request.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)); // 1-hour expiration

        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();  // Return the signed URL
    }


    public List<Movie> retrieverAll()
    {
       List<Movie> movies = movieDao.findAll();

       List<Movie> updatedMovies = movies.stream()
               .peek(movie -> {
                   String imageKey = extractFilename(movie.getImagePath());
                   String trailerKey = extractFilename(movie.getTrailerPath());

                   String signedImageUrl = getPreSignedUrl(imageKey);
                   String signedTrailerUrl = getPreSignedUrl(trailerKey);

                   movie.setImagePath(signedImageUrl);
                   movie.setTrailerPath(signedTrailerUrl);
               }).collect(Collectors.toList());

       return updatedMovies;
    }

    private String extractFilename(String path) {
        if (path == null || path.isEmpty()) return path;
        return path.substring(path.lastIndexOf('/') + 1);
    }
}