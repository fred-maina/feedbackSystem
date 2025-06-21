package com.example.modulith.simple.core.session;

import java.util.Optional;
import java.util.Set;

public interface AdminSessionRepository {

    void saveSession(String sessionId, String adminName);

    Optional<String> findAdminBySession(String sessionId);

    Optional<String> findSessionByAdmin(String adminName);

    boolean isAdminNameTaken(String adminName);

    void deleteSession(String sessionId);

    Set<String> findAllAdminNames(); // New method
}