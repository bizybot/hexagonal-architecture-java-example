package com.baeldung.domain.repository;

import java.util.UUID;

import com.baeldung.domain.Movie;

public interface MovieRepository {

    void save(Movie movie);

    Movie findById(UUID movieId);

    void delete(UUID movieId);
}
