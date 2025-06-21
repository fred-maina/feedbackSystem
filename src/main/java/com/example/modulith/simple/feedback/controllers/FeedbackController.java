package com.example.modulith.simple.feedback.controllers;

import com.example.modulith.simple.core.dto.DashboardDTO;
import com.example.modulith.simple.core.dto.FeedbackRequest;
import com.example.modulith.simple.core.session.AdminSessionRepository;
import com.example.modulith.simple.feedback.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final AdminSessionRepository adminRepo;

    @PostMapping
    public ResponseEntity<Void> receiveFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        try {
            feedbackService.sendFeedback(feedbackRequest.getAdminName(), feedbackRequest.getMessage(), feedbackRequest.getRating());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Error sending feedback via websocket", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid feedback request", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<?> myFeedbacks(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String sessionId = authorizationHeader.substring(7);
        return adminRepo.findAdminBySession(sessionId)
                .map(feedbackService::getDashboardData)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}