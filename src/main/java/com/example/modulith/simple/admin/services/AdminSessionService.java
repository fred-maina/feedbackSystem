package com.example.modulith.simple.admin.services;

import com.example.modulith.simple.core.session.AdminSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminSessionService {

    private final AdminSessionRepository repository;

    public void saveSession(String sessionId, String adminName) {
        repository.saveSession(sessionId, adminName);
    }

    public Optional<String> findAdminBySession(String sessionId) {
        return repository.findAdminBySession(sessionId);
    }

    public Optional<String> findSessionByAdmin(String adminName) {
        return repository.findSessionByAdmin(adminName);
    }

    public boolean isAdminNameTaken(String adminName) {
        return repository.isAdminNameTaken(adminName);
    }

    public void deleteSession(String sessionId) {
        repository.deleteSession(sessionId);
    }
}
