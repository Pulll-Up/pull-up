package com.pullup.interview.controller;

import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.request.PostCommentRequest;
import com.pullup.interview.dto.response.CommentsResponse;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.InterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResultResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.dto.response.PostCommentResponse;
import com.pullup.interview.dto.response.SearchedInterviewQuestionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<InterviewResponse> getTodayInterview();

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
    public ResponseEntity<CompletableFuture<MyInterviewAnswerResponse>> submitInterviewAnswer(Long interviewId,
                                                                                              MyInterviewAnswerRequest myInterviewAnswerRequest);

    @Operation(
            summary = "오늘의 문제 답변에 대한 결과 조회",
            description = "오늘의 문제 답변에 대한 결과를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 답변에 대한 결과 조회 성공",
                            content = @Content(schema = @Schema(implementation = MyInterviewAnswerResultResponse.class))
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
    public ResponseEntity<MyInterviewAnswerResultResponse> getMyInterviewAnswerResult(Long interviewAnswerId);

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
            summary = "오늘의 질문에 대한 답변 단건 조회",
            description = "오늘의 질문에 대한 답변 단건을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 질문에 대한 답변 단건 조회 성공",
                            content = @Content(schema = @Schema(implementation = InterviewAnswerResponse.class))
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
    public ResponseEntity<InterviewAnswerResponse> getInterviewAnswer(Long interviewAnswerId);

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
    public ResponseEntity<InterviewAnswersResponse> getInterviewAnswers(Long interviewId);

    @Operation(
            summary = "키워드로 오늘의 문제 리스트 검색",
            description = "키워드로 오늘의 문제 리스트를 검색합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 리스트 검색 성공",
                            content = @Content(schema = @Schema(implementation = SearchedInterviewQuestionsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<SearchedInterviewQuestionsResponse> getSearchedInterviewQuestions(String keyword);

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
                            description = "댓글 내용은 필수입니다.",
                            content = @Content(schema = @Schema(hidden = true))
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
    public ResponseEntity<PostCommentResponse> postComment(Long interviewId,
                                                           PostCommentRequest postCommentRequest);

    @Operation(
            summary = "오늘의 문제 답변에 대한 댓글 수정",
            description = "오늘의 문제 답변에 대한 댓글을 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 수정 성공",
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "댓글을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> modifyComment(Long commentId,
                                              PostCommentRequest postCommentRequest);

    @Operation(
            summary = "오늘의 문제 답변에 대한 댓글 삭제",
            description = "오늘의 문제 답변에 대한 댓글을 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "댓글을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<Void> deleteComment(Long commentId);

    @Operation(
            summary = "오늘의 문제 답변에 대한 댓글 전체 조회",
            description = "오늘의 문제 답변에 대한 댓글 전체를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 댓글 전체 조회 성공",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "권한이 없는 사용자입니다.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<CommentsResponse> getComments(Long interviewAnswerId);

    @Operation(
            summary = "오늘의 문제 답변에 대한 댓글 좋아요 토글",
            description = "오늘의 문제 댓글 좋아요를 토글합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 답변에 대한 댓글 좋아요 토글 성공",
                            content = @Content(schema = @Schema(hidden = true))
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
    public ResponseEntity<Void> toggleLike(Long interviewAnswerId);
}
