package com.example.modulith.simple.feedback.repositories;

import com.example.modulith.simple.core.dto.FeedbackDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisFeedbackRepository {

    private final RedisTemplate<String, Object> redis;
    private final ObjectMapper objectMapper;

    public void saveFeedback(String adminName, FeedbackDTO feedback) {
        redis.opsForList().leftPush("feedback:" + adminName, feedback);
    }

    public List<FeedbackDTO> getFeedbacks(String adminName) {
        List<Object> objects = redis.opsForList().range("feedback:" + adminName, 0, -1);
        if (objects == null) {
            return Collections.emptyList();
        }
        return objects.stream()
                .map(obj -> objectMapper.convertValue(obj, FeedbackDTO.class))
                .collect(Collectors.toList());
    }
}