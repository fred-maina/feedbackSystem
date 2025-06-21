package com.example.modulith.simple.admin.controllers;

import com.example.modulith.simple.admin.services.AdminSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminSessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String name) {
        if (sessionService.isAdminNameTaken(name)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Name already taken."));
        }

        String sessionId = UUID.randomUUID().toString();
        sessionService.saveSession(sessionId, name);

        return ResponseEntity.ok(Map.of("sessionId", sessionId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> whoAmI(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No active session"));
        }
        String sessionId = authorizationHeader.substring(7);
        return sessionService.findAdminBySession(sessionId)
                .map(name -> ResponseEntity.ok(Map.of("name", name)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid session")));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> doesAdminExist(@RequestParam String name) {
        if (sessionService.isAdminNameTaken(name)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}