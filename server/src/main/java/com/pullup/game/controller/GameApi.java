package com.pullup.game.controller;

import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.request.JoinRoomRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GetPlayerTypeResponse;
import com.pullup.game.dto.response.GetRandomMatchTypeResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Game", description = "게임 관련 API")
public interface GameApi {

    @Operation(
            summary = "초대 방식 게임방 생성",
            description = "초대 방식으로 게임방을 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "게임방 생성 성공",
                            content = @Content(schema = @Schema(implementation = CreateRoomResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody CreateRoomWithSubjectsRequest request);

    @Operation(
            summary = "랜덤 매칭 게임방 생성",
            description = "랜덤 매칭 방식으로 게임방을 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "랜덤 매칭 게임방 생성 성공",
                            content = @Content(schema = @Schema(implementation = CreateRoomResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<CreateRoomResponse> createRoomForRandomMatching();

    @Operation(
            summary = "게임방 참가",
            description = "사용자가 특정 게임방에 참가합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "게임방 참가 성공",
                            content = @Content(schema = @Schema(implementation = JoinRoomResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "이미 참가한 사용자이거나, 입장 불가능한 방입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<JoinRoomResponse> joinRoom(@RequestBody JoinRoomRequest joinRoomRequest);

    @Operation(
            summary = "플레이어 타입 조회",
            description = "게임방에서 사용자의 플레이어 타입(player1P, player2P)을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "플레이어 타입 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetPlayerTypeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "게임방을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetPlayerTypeResponse> getPlayerType(@PathVariable("roomId") String roomId);

    @Operation(
            summary = "랜덤 매칭 타입 조회",
            description = "사용자가 랜덤 매칭을 시도할 때, 새로운 방을 생성해야 하는지(JOIN/CREATE) 정보를 제공합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "랜덤 매칭 타입 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetRandomMatchTypeResponse.class))
                    )
            }
    )
    public ResponseEntity<GetRandomMatchTypeResponse> getRandomMatchType();

    @Operation(
            summary = "게임방 삭제",
            description = "게임방을 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게임방 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "게임방을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> deleteGameRoom(@PathVariable("roomId") String roomId);
}
