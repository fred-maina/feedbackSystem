package com.example.modulith.simple.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private String adminName;
    private String message;
    private int rating;
}