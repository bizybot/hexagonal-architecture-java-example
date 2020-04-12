package com.baeldung.domain;

import java.util.UUID;

public class Rating {

    private UUID id;
    private String reviewer;
    private int noOfStars;

    public Rating(UUID id, String reviewer, int noOfStars) {
        this.id = id;
        this.reviewer = reviewer;
        this.noOfStars = noOfStars;
    }

    public int getNoOfStars() {
        return noOfStars;
    }

    public String getReviewer() {
        return reviewer;
    }

    public UUID getId() {
        return id;
    }

}
