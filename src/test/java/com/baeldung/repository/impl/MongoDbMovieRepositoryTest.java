package com.baeldung.repository.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.baeldung.domain.Movie;
import com.baeldung.repository.impl.MongoDbMovieRepository;

public class MongoDbMovieRepositoryTest {
    private static MongoDbMovieRepository mongoDbMovieRepository;

    @BeforeAll
    public static void beforeAll() throws UnknownHostException, IOException {
        mongoDbMovieRepository = new MongoDbMovieRepository();
    }

    @Test
    public void whenMovieIsSaved_ThenVerifyIfItCanBeFoundById() {
        final UUID uuid = UUID.randomUUID();
        Movie movie = new Movie(uuid, "LOTR: The fellowship of the ring");
        movie.addRating("Yogesh", 10);
        movie.addRating("Jiya", 6);
        mongoDbMovieRepository.save(movie);

        Movie actual = mongoDbMovieRepository.findById(uuid);
        assertThat(actual.getName(), is(movie.getName()));
        assertThat(actual.getAllRatings()
            .size(),
            is(movie.getAllRatings()
                .size()));

        mongoDbMovieRepository.delete(uuid);
    }

    @Test
    public void whenMovieIsSaved_ThenVerifyItCanBeSearchedByName() {
        final UUID uuid1 = UUID.randomUUID();
        Movie movie1 = new Movie(uuid1, "LOTR: The fellowship of the ring");
        mongoDbMovieRepository.save(movie1);
        final UUID uuid2 = UUID.randomUUID();
        Movie movie2 = new Movie(uuid2, "LOTR: The two towers");
        mongoDbMovieRepository.save(movie2);

        Set<Movie> movies = mongoDbMovieRepository.searchMoviesByName("LOTR");
        assertThat(movies.size(), is(2));

        mongoDbMovieRepository.delete(uuid1);
        mongoDbMovieRepository.delete(uuid2);
    }

    @AfterAll
    public static void done() {
        mongoDbMovieRepository.shutDown();
    }
}
