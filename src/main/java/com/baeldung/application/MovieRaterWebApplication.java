package com.baeldung.application;

import static spark.Spark.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

import com.baeldung.application.service.impl.MovieSearchServiceImpl;
import com.baeldung.application.service.impl.MovieServiceImpl;
import com.baeldung.domain.Movie;
import com.baeldung.domain.service.MovieSearchService;
import com.baeldung.domain.service.MovieService;
import com.baeldung.repository.impl.MongoDbMovieRepository;
import com.google.gson.Gson;

/**
 * Sample web interface for movie rating system
 */
public class MovieRaterWebApplication {

    public static void main(String[] args) throws UnknownHostException, IOException {
        port(8181);

        final MongoDbMovieRepository mongoDbMovieRepository = new MongoDbMovieRepository();
        final MovieService movieService = new MovieServiceImpl(mongoDbMovieRepository);
        final MovieSearchService movieSearchService = new MovieSearchServiceImpl(mongoDbMovieRepository);
        final Gson gson = new Gson();

        post("/api/movies", (req, res) -> {
            String movieName = req.queryParams("movieName");
            res.type("application/json");
            if (movieName == null || movieName.trim()
                .length() == 0) {
                throw new AppException(400, "movieName must be provided");
            }

            Movie movie = movieSearchService.findByName(movieName);
            if (movie != null) {
                throw new AppException(409, "movieName already exists with id " + movie.getId());
            } else {
                UUID uuid = movieService.saveMovie(movieName);
                return gson.toJson(new AppResponse(200, "movie created with id " + uuid));
            }
        });

        put("/api/movies/:id/rating", (req, res) -> {
            String movieId = req.params("id");
            res.type("application/json");
            if (movieId == null || movieId.trim()
                .length() == 0) {
                throw new AppException(400, "movieId must be provided");
            }
            int noOfStars = 0;
            try {
                noOfStars = Integer.valueOf(req.queryParamOrDefault("noOfStars", "0"));
            } catch (NumberFormatException nfe) {
                throw new AppException(400, "noOfStars must be an integer");
            }
            String reviewer = req.queryParams("reviewer");
            if (reviewer == null || reviewer.trim()
                .length() == 0) {
                throw new AppException(400, "reviewer must be provided");
            }

            try {
                Movie movie = movieService.findMovieById(UUID.fromString(movieId));
                if (movie == null) {
                    throw new AppException(404, "movie with id " + movieId + " not found");
                }
                movieService.addRating(UUID.fromString(movieId), reviewer, noOfStars);
                return gson.toJson(new AppResponse(200, "Done"));
            } catch (IllegalArgumentException e) {
                throw new AppException(400, "invalid movie id provided");
            } catch (IllegalStateException e) {
                throw new AppException(404, "moview with id " + movieId + " not found");
            }
        });

        get("/api/movies/:id/avg_rating", (req, res) -> {
            String movieId = req.params("id");
            res.type("application/json");
            if (movieId == null || movieId.trim()
                .length() == 0) {
                throw new AppException(400, "movieId must be provided");
            }
            try {
                UUID uuid = UUID.fromString(movieId);
                Movie movie = movieService.findMovieById(uuid);
                if (movie == null) {
                    throw new AppException(404, "movie with id " + movieId + " not found");
                }
                return gson.toJson(new AppResponse(200, "movie : " + movie.getName() + " has average rating of : " + movieService.getAverageRating(uuid)));
            } catch (IllegalArgumentException e) {
                throw new AppException(400, "invalid movie id provided");
            } catch (IllegalStateException e) {
                throw new AppException(404, "moview with id " + movieId + " not found");
            }
        });

        exception(AppException.class, (exception, request, response) -> {
            response.status(exception.status);
            response.body(gson.toJson(new AppResponse(exception.status, exception.message)));
        });

    }

    static class AppResponse {
        int status;
        String message;

        AppResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    static class AppException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        int status;
        String message;

        AppException(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
