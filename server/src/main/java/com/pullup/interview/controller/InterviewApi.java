package com.pullup.interview.controller;

import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.dto.request.PostCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
                            content = @Content(schema = @Schema(implementation = MyInterviewAnswerResponse.class))
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
    public ResponseEntity<MyInterviewAnswerResponse> submitInterviewAnswer(@PathVariable Long interviewId,
                                                                           @Valid @RequestBody MyInterviewAnswerRequest myInterviewAnswerRequest);

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
                            description = "면접 답변을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<MyInterviewAnswersResponse> getMyInterviewAnswers();

    @Operation(
            summary = "오늘의 질문에 대한 답변 전체 조회",
            description = "오늘의 질문에 대한 답변 전체를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 질문에 대한 답변 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = InterviewAnswersResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "면접 답변을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<InterviewAnswersResponse> getInterviewAnswers(@PathVariable Long interviewId);

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
    public ResponseEntity<Void> postComment(@PathVariable Long interviewId, PostCommentRequest postCommentRequest);
}
