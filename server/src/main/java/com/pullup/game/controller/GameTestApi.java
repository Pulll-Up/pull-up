package com.pullup.game.controller;

import com.pullup.game.domain.GameRoom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import org.springframework.http.ResponseEntity;

@Tag(name = "GameTest", description = "게임 테스트 관련 API")
public interface GameTestApi {

    @Operation(
            summary = "모든 게임방 조회 (테스트용)",
            description = "현재 저장된 모든 게임방 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게임방 조회 성공",
                            content = @Content(schema = @Schema(implementation = GameRoom.class))
                    )
            }
    )
    public ResponseEntity<Collection<GameRoom>> getAllRooms();
}
