package com.pullup.member.controller;

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
}
