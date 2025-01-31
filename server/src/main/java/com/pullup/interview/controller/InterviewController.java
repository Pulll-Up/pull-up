package com.pullup.interview.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
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
    public ResponseEntity<MyInterviewAnswerResponse> submitInterviewAnswer(@PathVariable("interviewId") Long interviewId,
                                                                           @Valid @RequestBody MyInterviewAnswerRequest myInterviewAnswerRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MyInterviewAnswerResponse myInterviewAnswerResponse = interviewService.submitInterviewAnswer(memberId, interviewId,
                myInterviewAnswerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(myInterviewAnswerResponse);
    }

    @Override
    @GetMapping("/me/all")
    public ResponseEntity<MyInterviewAnswersResponse> getMyInterviewAnswers() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MyInterviewAnswersResponse interviewAnswersResponse = interviewService.getMyInterviewAnswers(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(interviewAnswersResponse);
    }

}