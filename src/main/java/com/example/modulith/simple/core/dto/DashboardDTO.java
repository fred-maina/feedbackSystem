package com.example.modulith.simple.core.dto;

import java.util.List;

public class DashboardDTO {
    private final List<FeedbackDTO> feedbacks;
    private final double averageRating;

    public DashboardDTO(List<FeedbackDTO> feedbacks, double averageRating) {
        this.feedbacks = feedbacks;
        this.averageRating = averageRating;
    }

    public List<FeedbackDTO> getFeedbacks() {
        return feedbacks;
    }

    public double getAverageRating() {
        return averageRating;
    }
}