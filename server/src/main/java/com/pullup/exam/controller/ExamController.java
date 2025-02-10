package com.pullup.exam.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.exam.dto.request.PostExamRequest;
import com.pullup.exam.dto.request.PostExamWithAnswerReqeust;
import com.pullup.exam.dto.response.GetExamDetailsResponse;
import com.pullup.exam.dto.response.GetExamPageResponse;
import com.pullup.exam.dto.response.GetExamResponse;
import com.pullup.exam.dto.response.GetExamResultResponse;
import com.pullup.exam.dto.response.GetExamScoresResponse;
import com.pullup.exam.dto.response.GetExamStrengthResponse;
import com.pullup.exam.dto.response.PostExamResponse;
import com.pullup.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("/{examId}")
    public ResponseEntity<GetExamDetailsResponse> getExamDetails(@PathVariable("examId") Long id) {
        GetExamDetailsResponse getExamDetailsResponse = examService.getExamDetails(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamDetailsResponse);
    }

    @PostMapping("/me")
    public ResponseEntity<PostExamResponse> postExam(@Valid @RequestBody PostExamRequest postExamRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        PostExamResponse postExamResponse = examService.postExam(postExamRequest, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postExamResponse);
    }

    @PostMapping("/{examId}")
    public ResponseEntity<Void> postExamWithAnswer(@PathVariable("examId") Long id,
                                                   @RequestBody PostExamWithAnswerReqeust postExamWithAnswerReqeust) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        examService.postExamWithAnswer(id, postExamWithAnswerReqeust, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{examId}/result")
    public ResponseEntity<GetExamResultResponse> getExamResult(@PathVariable("examId") Long id) {
        GetExamResultResponse getExamResultResponse = examService.getExamResult(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamResultResponse);
    }

    @GetMapping("/me/recent")
    public ResponseEntity<GetExamResponse> getRecentExam() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetExamResponse getExamResponse = examService.getExam(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<GetExamPageResponse> getExamPageOrderByCreatedAt(
            @PageableDefault(size = 10) Pageable pageable) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetExamPageResponse getExamPageResponse = examService.getExamPageOrderByCreatedAt(pageable, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamPageResponse);
    }


    @GetMapping("/me/score")
    public ResponseEntity<GetExamScoresResponse> getRecentFiveExamScores() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetExamScoresResponse getExamScoresResponse = examService.getRecentFiveExamScores(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamScoresResponse);
    }

    @GetMapping("/me/correct-rate")
    public ResponseEntity<GetExamStrengthResponse> getExamStrength() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetExamStrengthResponse getExamStrengthResponse = examService.getExamStrength(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamStrengthResponse);
    }

}
