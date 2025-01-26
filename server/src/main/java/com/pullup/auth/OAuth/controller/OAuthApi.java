package com.pullup.auth.OAuth.controller;


import com.pullup.auth.OAuth.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "OAuth", description = "OAuth 관련 API")
public interface OAuthApi {

    @Operation(
            summary = "로그인 상태 및 오늘 문제 풀이 여부 확인",
            description = "계정 존재 여부와 오늘의 문제 풀이 여부를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 상태 및 문제 풀이 여부 반환",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "계정 존재하지 않은 경우",
                                                    value = "{\"isSignedUp\": false, \"isSolvedToday\": false}"
                                            ),
                                            @ExampleObject(
                                                    name = "계정은 있지만 문제를 풀지 않은 경우",
                                                    value = "{\"isSignedUp\": true, \"isSolvedToday\": false}"
                                            ),
                                            @ExampleObject(
                                                    name = "계정도 있고 문제도 푼 경우",
                                                    value = "{\"isSignedUp\": true, \"isSolvedToday\": true}"
                                            )
                                    },
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    ResponseEntity<LoginResponse> signIn(HttpServletRequest request, HttpServletResponse response);
}
