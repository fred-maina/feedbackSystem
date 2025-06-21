package com.example.modulith.simple.feedback.services;


import com.example.modulith.simple.core.dto.DashboardDTO;
import com.example.modulith.simple.core.dto.FeedbackDTO;
import com.example.modulith.simple.core.session.AdminSessionRepository;
import com.example.modulith.simple.feedback.repositories.RedisFeedbackRepository;
import com.example.modulith.simple.infra.config.AdminWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final AdminSessionRepository adminRepo;
    private final RedisFeedbackRepository feedbackRepo;
    private final AdminWebSocketHandler socketHandler;

    public void sendFeedback(String adminName, String message, int rating) throws IOException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        FeedbackDTO dto = new FeedbackDTO(message, Instant.now(), rating);
        feedbackRepo.saveFeedback(adminName, dto);

        adminRepo.findSessionByAdmin(adminName).ifPresent(sessionId -> {
            try {
                socketHandler.sendToAdmin(sessionId, dto);
            } catch (IOException e) {
                log.error("Failed to send feedback to admin", e);
            }
        });
    }

    public DashboardDTO getDashboardData(String adminName) {
        List<FeedbackDTO> feedbacks = feedbackRepo.getFeedbacks(adminName);
        double average = feedbacks.stream()
                .mapToInt(FeedbackDTO::getRating)
                .average()
                .orElse(0.0);
        return new DashboardDTO(feedbacks, average);
    }
}