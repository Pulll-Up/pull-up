package com.pullup.member.controller;

import com.pullup.member.dto.request.DeviceTokenRequest;
import com.pullup.member.dto.request.InterestSubjectsRequest;
import com.pullup.member.dto.response.MemberProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "member", description = "회원 API")
public interface MemberApi {

    @Operation(
            summary = "회원 프로필 조회",
            description = "회원 프로필을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 프로필 조회 성공",
                            content = @Content(schema = @Schema(implementation = MemberProfileResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<MemberProfileResponse> getMemberProfile();

    @Operation(
            summary = "관심 과목 수정",
            description = "회원의 관심 과목을 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관심 과목 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "과목 리스트는 null일 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "최소 한 개 이상의 과목을 입력해야 합니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> updateInterestSubject(InterestSubjectsRequest interestSubjectsRequest);

    @Operation(
            summary = "Device Token 저장",
            description = "Device Token을 저장합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Device Token 저장 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Device Token은 null일 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> registerDeviceToken(DeviceTokenRequest deviceTokenRequest);
}