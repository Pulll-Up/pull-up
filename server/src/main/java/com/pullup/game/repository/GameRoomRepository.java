package com.pullup.game.repository;

import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.ProblemCard;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameRoomRepository {
    private final ConcurrentHashMap<String, GameRoom> gameRooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<ProblemCard>> gameProblems = new ConcurrentHashMap<>();

    public GameRoom save(GameRoom gameRoom) {
        gameRooms.put(gameRoom.getRoomId(), gameRoom);
        return gameRoom;
    }

    public Optional<GameRoom> findByRoomId(String roomId) {
        return Optional.ofNullable(gameRooms.get(roomId));
    }

    public void deleteById(String roomId) {
        gameRooms.remove(roomId);
    }

    public boolean existsById(String roomId) {
        return gameRooms.containsKey(roomId);
    }

    public Collection<GameRoom> getAllRooms() {
        return gameRooms.values();
    }

    // 문제 리스트 저장
    public void saveProblems(String roomId, List<ProblemCard> problems) {
        gameProblems.put(roomId, problems);
    }

    // 문제 리스트 조회
    public List<ProblemCard> getProblemsByRoomId(String roomId) {
        return gameProblems.getOrDefault(roomId, List.of());
    }

    // 방 삭제 시 문제 리스트 제거
    public void deleteRoom(String roomId) {
        gameProblems.remove(roomId);
    }

    // 미사용 방 정리를 위한 메서드
    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void cleanupStaleRooms() {
        long currentTimeMillis = System.currentTimeMillis();

        gameRooms.entrySet().removeIf(entry -> {
            GameRoom room = entry.getValue();

            // LocalDateTime을 밀리초로 변환
            long createdAtMillis = room.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 30분 이상 된 방이거나 게임이 끝난 방 삭제
            return (currentTimeMillis - createdAtMillis > 30 * 60 * 1000)
                    || room.getStatus() == GameRoomStatus.FINISHED;
        });
    }
}