package com.pullup.member.controller;

import com.pullup.member.dto.request.PostCommentRequest;
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
            summary = "오늘의 문제에 대한 댓글 작성",
            description = "오늘의 문제에 대한 댓글을 작성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 작성 성공",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "해당 오늘의 문제가 존재하지 않습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "오늘의 문제의 번호는 필수입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "댓글 내용은 필수입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> postComment(PostCommentRequest postCommentRequest);
}
