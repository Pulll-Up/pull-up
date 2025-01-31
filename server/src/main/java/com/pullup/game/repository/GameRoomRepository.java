package com.pullup.game.repository;

import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameStatus;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameRoomRepository {
    private final ConcurrentHashMap<String, GameRoom> gameRooms = new ConcurrentHashMap<>();

    public GameRoom save(GameRoom gameRoom) {
        gameRooms.put(gameRoom.getRoomId(), gameRoom);
        return gameRoom;
    }

    public Optional<GameRoom> findByRoomId(String roomId) {
        return Optional.ofNullable(gameRooms.get(roomId));
    }

    public void remove(String roomCode) {
        gameRooms.remove(roomCode);
    }

    // 미사용 방 정리를 위한 메서드
    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void cleanupStaleRooms() {
        long currentTimeMillis = System.currentTimeMillis();

        gameRooms.entrySet().removeIf(entry -> {
            GameRoom room = entry.getValue();

            // ✅ LocalDateTime을 밀리초로 변환
            long createdAtMillis = room.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 30분 이상 된 방이거나 게임이 끝난 방 삭제
            return (currentTimeMillis - createdAtMillis > 30 * 60 * 1000)
                    || room.getStatus() == GameStatus.FINISHED;
        });
    }
}