package com.baeldung.application.service.impl;

import java.util.UUID;

import com.baeldung.domain.Movie;
import com.baeldung.domain.repository.MovieRepository;
import com.baeldung.domain.service.MovieService;

public class MovieServiceImpl implements MovieService {
    private MovieRepository moviesRepository;

    public MovieServiceImpl(MovieRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public UUID saveMovie(String movieName) {
        Movie movie = new Movie(UUID.randomUUID(), movieName);
        moviesRepository.save(movie);
        return movie.getId();
    }

    @Override
    public UUID addRating(UUID movieId, String reviewer, int noOfStars) {
        Movie movie = moviesRepository.findById(movieId);
        if (movie == null) {
            throw new IllegalStateException();
        }
        UUID ratingId = movie.addRating(reviewer, noOfStars);
        moviesRepository.save(movie);
        return ratingId;
    }

    @Override
    public Movie findMovieById(UUID movieId) {
        return moviesRepository.findById(movieId);
    }

    @Override
    public double getAverageRating(UUID movieId) {
        Movie movie = moviesRepository.findById(movieId);
        if (movie == null) {
            throw new IllegalStateException();
        }
        return movie.getAverageRating();
    }
}
