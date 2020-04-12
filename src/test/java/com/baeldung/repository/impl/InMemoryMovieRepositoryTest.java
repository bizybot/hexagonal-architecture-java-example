package com.baeldung.repository.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.baeldung.domain.Movie;
import com.baeldung.repository.impl.InMemoryMovieRepository;

public class InMemoryMovieRepositoryTest {
    private InMemoryMovieRepository inMemoryMovieRepository = new InMemoryMovieRepository();

    @Test
    public void whenMovieIsSaved_ThenVerifyIfItCanBeFoundById() {
        final UUID uuid = UUID.randomUUID();
        Movie movie = new Movie(uuid, "LOTR: The fellowship of the ring");
        inMemoryMovieRepository.save(movie);

        Movie actual = inMemoryMovieRepository.findById(uuid);
        assertThat(actual.getName(), is(movie.getName()));
        assertThat(actual.getAllRatings()
            .size(),
            is(movie.getAllRatings()
                .size()));

        inMemoryMovieRepository.delete(uuid);
    }

    @Test
    public void whenMovieIsSaved_ThenVerifyItCanBeSearchedByName() {
        final UUID uuid1 = UUID.randomUUID();
        Movie movie1 = new Movie(uuid1, "LOTR: The fellowship of the ring");
        inMemoryMovieRepository.save(movie1);
        final UUID uuid2 = UUID.randomUUID();
        Movie movie2 = new Movie(uuid2, "LOTR: The two towers");
        inMemoryMovieRepository.save(movie2);

        Set<Movie> movies = inMemoryMovieRepository.searchMoviesByName("LOTR");
        assertThat(movies.size(), is(2));

        inMemoryMovieRepository.delete(uuid1);
        inMemoryMovieRepository.delete(uuid2);
    }
}
