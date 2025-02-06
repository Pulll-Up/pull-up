package com.pullup.auth.oAuth.controller;


import com.pullup.auth.oAuth.dto.request.SignUpRequest;
import com.pullup.auth.oAuth.dto.response.LoginResponse;
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
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    ResponseEntity<LoginResponse> signIn(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            summary = "회원가입",
            description = "사용자의 선호 과목을 입력받아 회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원가입 실패",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    ResponseEntity<Void> signUp(SignUpRequest signUpRequest);
}
