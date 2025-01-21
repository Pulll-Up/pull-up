package com.pullup.exam.controller;

import com.pullup.exam.dto.ExamDetailsDto;
import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.PostExamDto;
import com.pullup.exam.service.ExamService;
import java.util.List;
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

    @GetMapping("/{examId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetExamDetailsResponse> getExamDetails(@PathVariable("examId") Long id) {
        GetExamDetailsResponse getExamDetailsResponse = examService.getExamDetails(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getExamDetailsResponse);
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> postExam(@RequestBody PostExamDto postExamDto) {
        // 값 검증하기 - 6개 중 하나인지 아닌지, 상중하인지 아닌지
        Long examId = examService.postExam(postExamDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examId);
    }

}
