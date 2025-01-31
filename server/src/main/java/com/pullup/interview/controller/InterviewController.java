package com.pullup.interview.controller;

import com.pullup.interview.dto.request.InterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.service.InterviewService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interview")
public class InterviewController implements InterviewApi {

    private final InterviewService interviewService;

    @Override
    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewResponse> getTodayInterview(@PathVariable("interviewId") Long interviewId) {
        InterviewResponse todayInterviewResponse = interviewService.getTodayInterview(interviewId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(todayInterviewResponse);
    }

    @Override
    @PostMapping("/{interviewId}/submit")
    public ResponseEntity<InterviewAnswerResponse> submitInterviewAnswer(@PathVariable("interviewId") Long interviewId,
                                                                         @Valid @RequestBody InterviewAnswerRequest interviewAnswerRequest) {
        InterviewAnswerResponse interviewAnswerResponse = interviewService.submitInterviewAnswer(interviewId, interviewAnswerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewAnswerResponse);
    }

}