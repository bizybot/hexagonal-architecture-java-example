package com.baeldung.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Movie {

    private UUID id;
    private String name;
    private List<Rating> ratings = new ArrayList<>();

    public Movie(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID addRating(String reviewer, int noOfStars) {
        Rating rating = new Rating(UUID.randomUUID(), reviewer, noOfStars);
        this.ratings.add(rating);
        return rating.getId();
    }

    public List<Rating> getAllRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public double getAverageRating() {
        return getAllRatings().stream()
            .mapToInt(r -> r.getNoOfStars())
            .summaryStatistics()
            .getAverage();
    }
}
