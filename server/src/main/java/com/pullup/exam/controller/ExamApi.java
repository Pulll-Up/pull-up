package com.pullup.exam.controller;

import com.pullup.exam.dto.request.PostExamRequest;
import com.pullup.exam.dto.request.PostExamWithAnswerReqeust;
import com.pullup.exam.dto.response.GetAllExamResponse;
import com.pullup.exam.dto.response.GetExamDetailsResponse;
import com.pullup.exam.dto.response.GetExamPageResponse;
import com.pullup.exam.dto.response.GetExamResponse;
import com.pullup.exam.dto.response.GetExamResultResponse;
import com.pullup.exam.dto.response.GetExamScoresResponse;
import com.pullup.exam.dto.response.GetExamStrengthResponse;
import com.pullup.exam.dto.response.PostExamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Exam", description = "모의고사 관련 API")
public interface ExamApi {

    @Operation(
            summary = "모의고사 상세 조회",
            description = "특정 모의고사의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모의고사 상세 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamDetailsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "모의고사를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamDetailsResponse> getExamDetails(@PathVariable("examId") Long id);

    @Operation(
            summary = "모의고사 생성",
            description = "새로운 모의고사를 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "모의고사 생성 성공",
                            content = @Content(schema = @Schema(implementation = PostExamResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<PostExamResponse> postExam(@RequestBody PostExamRequest postExamRequest);

    @Operation(
            summary = "사용자가 최근에 푼 순서대로 모든 모의고사 조회",
            description = "사용자가 최근에 푼 순서대로 모든 모의고사를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "최근에 푼 순서대로 사용자의 모든 모의고사 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetAllExamResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetAllExamResponse> getAllExamOrderByCreatedAtDesc();

    @Operation(
            summary = "모의고사 제출",
            description = "사용자가 특정 모의고사에 대한 답안을 제출합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "모의고사 제출 성공",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "모의고사를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> postExamWithAnswer(
            @PathVariable("examId") Long id,
            @RequestBody PostExamWithAnswerReqeust postExamWithAnswerReqeust
    );

    @Operation(
            summary = "모의고사 결과 조회",
            description = "특정 모의고사의 결과를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모의고사 결과 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamResultResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "모의고사를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamResultResponse> getExamResult(@PathVariable("examId") Long id);

    @Operation(
            summary = "사용자의 최근 모의고사 조회",
            description = "사용자가 응시한 가장 최근 모의고사를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "최근 모의고사 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamResponse> getRecentExam();

    @Operation(
            summary = "모의고사 페이지네이션 조회",
            description = "사용자가 응시한 모의고사를 페이지네이션하여 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모의고사 페이지네이션 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamPageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamPageResponse> getExamPageOrderByCreatedAt(Pageable pageable);

    @Operation(
            summary = "최근 5개 모의고사 점수 조회",
            description = "사용자가 최근 응시한 5개의 모의고사 점수를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "최근 5개 모의고사 점수 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamScoresResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamScoresResponse> getRecentFiveExamScores();

    @Operation(
            summary = "사용자의 과목별 정답률 조회",
            description = "사용자의 과목별 정답률을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "과목별 정답률 조회 성공",
                            content = @Content(schema = @Schema(implementation = GetExamStrengthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<GetExamStrengthResponse> getExamStrength();
}
