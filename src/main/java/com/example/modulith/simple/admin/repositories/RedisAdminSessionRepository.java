package com.example.modulith.simple.admin.repositories;

import com.example.modulith.simple.core.session.AdminSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisAdminSessionRepository implements AdminSessionRepository {

    private final StringRedisTemplate redis;

    @Override
    public Set<String> findAllAdminNames() {
        Set<String> keys = redis.keys("admin:*");
        if (keys == null) {
            return Set.of();
        }
        return keys.stream()
                .map(key -> key.substring("admin:".length()))
                .collect(Collectors.toSet());
    }

    // ... other existing methods ...
    @Override
    public void saveSession(String sessionId, String adminName) {
        redis.opsForValue().set("session:" + sessionId, adminName);
        redis.opsForValue().set("admin:" + adminName, sessionId);
    }

    @Override
    public Optional<String> findAdminBySession(String sessionId) {
        return Optional.ofNullable(redis.opsForValue().get("session:" + sessionId));
    }

    @Override
    public Optional<String> findSessionByAdmin(String adminName) {
        return Optional.ofNullable(redis.opsForValue().get("admin:" + adminName));
    }

    @Override
    public boolean isAdminNameTaken(String adminName) {
        return redis.hasKey("admin:" + adminName);
    }

    @Override
    public void deleteSession(String sessionId) {
        findAdminBySession(sessionId).ifPresent(admin -> {
            redis.delete("session:" + sessionId);
            redis.delete("admin:" + admin);
        });
    }
}