package com.pullup.exam.controller;

import com.pullup.exam.dto.ExamWithAnswerReqeust;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.PostExamRequest;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private static final Long TEMP_MEMBER_ID = 1L;

    @GetMapping("/{examId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetExamDetailsResponse> getExamDetails(@PathVariable("examId") Long id) {
        GetExamDetailsResponse getExamDetailsResponse = examService.getExamDetails(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamDetailsResponse);
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> postExam(@Valid @RequestBody PostExamRequest postExamRequest) {
        Long memberId = TEMP_MEMBER_ID;
        Long examId = examService.postExam(postExamRequest, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examId);
    }

    @PostMapping("/{examId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> postExamWithAnswer(@PathVariable("examId") Long id, @RequestBody ExamWithAnswerReqeust examWithAnswerReqeust) {
        examService.postExamWithAnswer(id, examWithAnswerReqeust);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
