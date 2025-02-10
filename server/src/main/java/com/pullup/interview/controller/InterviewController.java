package com.pullup.interview.controller;

import com.pullup.common.util.SecurityUtil;
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
import com.pullup.interview.service.CommentService;
import com.pullup.interview.service.InterviewService;
import com.pullup.interview.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interview")
public class InterviewController implements InterviewApi {

    private final InterviewService interviewService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Override
    @GetMapping
    public ResponseEntity<InterviewResponse> getTodayInterview() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        InterviewResponse todayInterviewResponse = interviewService.getTodayInterview(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(todayInterviewResponse);
    }

    @Override
    @PostMapping("/{interviewId}/submit")
    public ResponseEntity<MyInterviewAnswerResponse> submitInterviewAnswer(
            @PathVariable("interviewId") Long interviewId,
            @Valid @RequestBody MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MyInterviewAnswerResponse myInterviewAnswerResponse = interviewService.submitInterviewAnswer(memberId,
                interviewId,
                myInterviewAnswerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(myInterviewAnswerResponse);
    }

    @Override
    @GetMapping("/{interviewAnswerId}/result")
    public ResponseEntity<MyInterviewAnswerResultResponse> getMyInterviewAnswerResult(
            @PathVariable("interviewAnswerId") Long interviewAnswerId
    ) {
        MyInterviewAnswerResultResponse myInterviewAnswerResultResponse = interviewService.getMyInterviewAnswerResult(
                interviewAnswerId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(myInterviewAnswerResultResponse);
    }

    @Override
    @GetMapping("/me/all")
    public ResponseEntity<MyInterviewAnswersResponse> getMyInterviewAnswers() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MyInterviewAnswersResponse interviewAnswersResponse = interviewService.getMyInterviewAnswers(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(interviewAnswersResponse);
    }

    @Override
    @GetMapping("/{interviewAnswerId}")
    public ResponseEntity<InterviewAnswerResponse> getInterviewAnswer(
            @PathVariable("interviewAnswerId") Long interviewAnswerId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        InterviewAnswerResponse interviewAnswerResponse = interviewService.getInterviewAnswer(
                memberId, interviewAnswerId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(interviewAnswerResponse);
    }

    @Override
    @GetMapping("{interviewId}/all")
    public ResponseEntity<InterviewAnswersResponse> getInterviewAnswers(@PathVariable("interviewId") Long interviewId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        InterviewAnswersResponse interviewAnswersResponse = interviewService.getInterviewAnswers(memberId, interviewId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(interviewAnswersResponse);
    }

    @Override
    @PostMapping("/{interviewId}/comment")
    public ResponseEntity<PostCommentResponse> postComment(@PathVariable("interviewId") Long interviewId,
                                                           @Valid @RequestBody PostCommentRequest postCommentRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        PostCommentResponse postCommentResponse = commentService.postComment(memberId, interviewId, postCommentRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postCommentResponse);
    }

    @Override
    @PatchMapping("/interview-answer/comment/{commentId}")
    public ResponseEntity<Void> modifyComment(@PathVariable("commentId") Long commentId,
                                              @Valid @RequestBody PostCommentRequest postCommentRequest) {
        commentService.modifyComment(commentId, postCommentRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping("/interview-answer/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @GetMapping("/{interviewAnswerId}/comment/all")
    public ResponseEntity<CommentsResponse> getComments(@PathVariable("interviewAnswerId") Long interviewAnswerId) {
        CommentsResponse commentsResponse = commentService.getComments(interviewAnswerId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentsResponse);
    }

    @Override
    @PostMapping("/{interviewAnswerId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable("interviewAnswerId") Long interviewAnswerId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        likeService.toggleLike(memberId, interviewAnswerId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}