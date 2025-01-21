package com.pullup.exam.controller;

import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.service.ExamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("/{examId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GetExamDetailsResponse>> getExamDetails(@PathVariable("examId") Long id) {
        List<GetExamDetailsResponse> getExamDetailsResponses = examService.getExamDetails(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamDetailsResponses);
    }

}
