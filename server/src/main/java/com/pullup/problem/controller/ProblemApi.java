package com.pullup.problem.controller;

import com.pullup.problem.dto.response.GetAllWrongProblemsResponse;
import com.pullup.problem.dto.response.GetBookmarkedProblemsResponse;
import com.pullup.problem.dto.response.GetProblemResponse;
import com.pullup.problem.dto.response.GetRecentWrongProblemsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Problem", description = "모의고사 문제 관련 API")
public interface ProblemApi {

    @Operation(
            summary = "모의고사 문제 북마크 토글",
            description = "모의고사 문제의 북마크를 설정 또는 해제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "북마크 토글 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "모의고사 문제를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> toggleProblemBookmark(@PathVariable("problemId") Long problemId);

    @Operation(
            summary = "북마크한 모의고사 문제 목록 조회",
            description = "사용자가 북마크한 모의고사 문제들을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "북마크한 모의고사 문제 목록 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetBookmarkedProblemsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetBookmarkedProblemsResponse> getBookmarkedProblems();

    @Operation(
            summary = "모의고사 문제 전체 조회",
            description = "모의고사 문제 전체를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모의고사 문제 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetProblemResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "모의고사 문제를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetProblemResponse> getProblem(@PathVariable("problemId") Long problemId);

    @Operation(
            summary = "최근 틀린 모의고사 문제 조회",
            description = "사용자가 최근에 틀린 모의고사 문제들을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "최근 틀린 모의고사 문제 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetRecentWrongProblemsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetRecentWrongProblemsResponse> getRecentWrongProblems();

    @Operation(
            summary = "모의고사 틀린 문제 전체 조회",
            description = "사용자가 틀린 모든 모의고사 문제들을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모의고사 틀린 문제 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetAllWrongProblemsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetAllWrongProblemsResponse> getAllWrongProblems();
}
