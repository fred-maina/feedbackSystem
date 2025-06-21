package com.example.modulith.simple.infra.config;

import com.example.modulith.simple.core.dto.FeedbackDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AdminWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = getQueryParam(session);
        if (sessionId != null) {
            sessions.put(sessionId, session);
        }
    }

    public void sendToAdmin(String sessionId, FeedbackDTO feedback) throws IOException {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            String payload = objectMapper.writeValueAsString(feedback);
            session.sendMessage(new TextMessage(payload));
        }
    }

    private String getQueryParam(WebSocketSession session) {
        if (session.getUri() == null) return null;
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("sessionId")) {
                return pair[1];
            }
        }
        return null;
    }
}