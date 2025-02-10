package com.pullup.game.repository;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class WebSocketSessionRepository {

    // sessionId -> playerId 매핑 저장
    private final ConcurrentHashMap<String, Long> sessionPlayerMap = new ConcurrentHashMap<>();

    public void save(String sessionId, Long playerId) {
        sessionPlayerMap.put(sessionId, playerId);
    }

    public Long findPlayerIdBySessionId(String sessionId) {
        return sessionPlayerMap.get(sessionId);
    }

    public void deleteBySessionId(String sessionId) {
        sessionPlayerMap.remove(sessionId);
    }
}