package com.pullup.exam.controller;

import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.GetExamResultResponse;
import com.pullup.exam.dto.PostExamRequest;
import com.pullup.exam.dto.PostExamWithAnswerReqeust;
import com.pullup.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pullup.exam.dto.GetExamScoresResponse;
import com.pullup.exam.dto.GetExamStrengthResponse;
@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private static final Long TEMP_MEMBER_ID = 1L;

    @GetMapping("/{examId}")
    public ResponseEntity<GetExamDetailsResponse> getExamDetails(@PathVariable("examId") Long id) {
        GetExamDetailsResponse getExamDetailsResponse = examService.getExamDetails(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamDetailsResponse);
    }

    @PostMapping("/me")
    public ResponseEntity<Long> postExam(@Valid @RequestBody PostExamRequest postExamRequest) {
        Long memberId = TEMP_MEMBER_ID;
        Long examId = examService.postExam(postExamRequest, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examId);
    }

    @PostMapping("/{examId}")
    public ResponseEntity<Void> postExamWithAnswer(@PathVariable("examId") Long id,
                                                   @RequestBody PostExamWithAnswerReqeust postExamWithAnswerReqeust) {
        Long memberId = TEMP_MEMBER_ID;
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


    @GetMapping("/me/score")
    public ResponseEntity<GetExamScoresResponse> getRecentFiveExamScores() {
        Long memberId = TEMP_MEMBER_ID;
        GetExamScoresResponse getExamScoresResponse = examService.getRecentFiveExamScores(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamScoresResponse);
    }

    @GetMapping("/me/correct-rate")
    public ResponseEntity<GetExamStrengthResponse> getExamStrength() {
        GetExamStrengthResponse getExamStrengthResponse = examService.getExamStrength(TEMP_MEMBER_ID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamStrengthResponse);
    }

}