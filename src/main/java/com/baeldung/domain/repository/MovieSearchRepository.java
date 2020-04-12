package com.baeldung.domain.repository;

import java.util.Set;

import com.baeldung.domain.Movie;

public interface MovieSearchRepository {

    Movie findMovieByName(String movieName);

    Set<Movie> searchMoviesByName(String movieName);
}
