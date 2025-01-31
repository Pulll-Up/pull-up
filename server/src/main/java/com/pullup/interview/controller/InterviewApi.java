package com.pullup.interview.controller;

import com.pullup.interview.dto.request.InterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Interview", description = "오늘의 문제 관련 API")
public interface InterviewApi {

    @Operation(
            summary = "오늘의 문제 조회",
            description = "오늘의 문제를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 조회 성공",
                            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "면접 질문을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<InterviewResponse> getTodayInterview(@PathVariable Long interviewId);

    @Operation(
            summary = "오늘의 문제 제출",
            description = "오늘의 문제를 제출합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 제출 성공",
                            content = @Content(schema = @Schema(implementation = InterviewAnswerResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "면접 질문을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<InterviewAnswerResponse> submitInterviewAnswer(@PathVariable Long interviewId,
                                                                         @Valid @RequestBody InterviewAnswerRequest interviewAnswerRequest);

    @Operation(
            summary = "멤버가 풀었던 오늘의 문제 전체 조회",
            description = "멤버가 풀었던 오늘의 문제 전체를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "멤버가 풀었던 오늘의 문제 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = MyInterviewAnswersResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "면접 질문을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<MyInterviewAnswersResponse> getMyInterviewAnswers();
}
