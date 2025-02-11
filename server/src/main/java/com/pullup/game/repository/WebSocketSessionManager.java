package com.pullup.game.repository;

import com.pullup.game.dto.PlayerSessionInfo;
import com.pullup.game.dto.response.PlayerType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSessionManager {
    private final Map<String, PlayerSessionInfo> sessionMap = new ConcurrentHashMap<>();

    public void addSession(String sessionId, String roomId, PlayerType playerType) {
        sessionMap.put(sessionId, new PlayerSessionInfo(roomId, playerType));
    }

    public PlayerSessionInfo removeSession(String sessionId) {
        return sessionMap.remove(sessionId); // 세션 삭제 후 (roomId, playerType) 반환
    }

    public PlayerSessionInfo getSessionInfo(String sessionId) {
        return sessionMap.get(sessionId);
    }
}
