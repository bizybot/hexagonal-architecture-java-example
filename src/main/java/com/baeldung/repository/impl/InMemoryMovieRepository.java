package com.baeldung.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.baeldung.domain.Movie;
import com.baeldung.domain.repository.MovieRepository;
import com.baeldung.domain.repository.MovieSearchRepository;

import java.util.Set;
import java.util.UUID;

public class InMemoryMovieRepository implements MovieRepository, MovieSearchRepository {
    private Map<UUID, Movie> moviesDB = new HashMap<>();

    @Override
    public Set<Movie> searchMoviesByName(String movieName) {
        return moviesDB.entrySet()
            .stream()
            .filter(entry -> entry.getValue()
                .getName()
                .contains(movieName))
            .map(Entry::getValue)
            .collect(Collectors.toSet());
    }

    @Override
    public void save(Movie movie) {
        moviesDB.put(movie.getId(), movie);
    }

    @Override
    public Movie findById(UUID movieId) {
        return moviesDB.get(movieId);
    }

    @Override
    public Movie findMovieByName(String movieName) {
        return moviesDB.entrySet()
            .stream()
            .filter(entry -> entry.getValue()
                .getName()
                .equals(movieName))
            .map(Entry::getValue)
            .findFirst()
            .orElse(null);
    }

    @Override
    public void delete(UUID movieId) {
        moviesDB.remove(movieId);
    }

}
