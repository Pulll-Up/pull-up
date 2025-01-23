package com.pullup.problem.controller;

import com.pullup.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problem")
@RequiredArgsConstructor
public class ProblemController {
    private static final Long TEMP_MEMBER_ID = 1L;
    private final ProblemService problemService;

    @PostMapping("/{problemId}")
    public ResponseEntity<Void> toggleProblemBookmark(@PathVariable("problemId") Long problemId) {
        problemService.toggleProblemBookmark(problemId, TEMP_MEMBER_ID);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
