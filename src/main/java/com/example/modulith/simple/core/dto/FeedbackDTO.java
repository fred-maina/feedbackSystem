package com.example.modulith.simple.core.dto;

import java.time.Instant;

public class FeedbackDTO {
    private String message;
    private Instant timestamp;
    private int rating; // must be 1 to 5

    public FeedbackDTO() {
    }

    public FeedbackDTO(String message, Instant timestamp, int rating) {
        this.message = message;
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getRating() {
        return rating;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }
}
