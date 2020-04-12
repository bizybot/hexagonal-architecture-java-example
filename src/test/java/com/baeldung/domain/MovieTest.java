package com.baeldung.domain;

import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.baeldung.domain.Movie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MovieTest {

    @Test
    public void givenMovie_WhenRatingsAreAdded_ThenVerifyAverageRatingIsCalculatedCorrectly() {
        Movie movie = new Movie(UUID.randomUUID(), "Terminator");
        movie.addRating("Yogesh", 9);
        movie.addRating("Gia", 8);

        assertThat(movie.getAllRatings()
            .size(), is(2));
        assertThat(movie.getAverageRating(), is(IntStream.of(9, 8)
            .summaryStatistics()
            .getAverage()));
    }
}
