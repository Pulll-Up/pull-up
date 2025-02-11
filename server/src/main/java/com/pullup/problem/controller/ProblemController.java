package com.pullup.problem.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.problem.dto.GetAllWrongProblemsResponse;
import com.pullup.problem.dto.GetBookmarkedProblemsResponse;
import com.pullup.problem.dto.GetProblemResponse;
import com.pullup.problem.dto.GetRecentWrongProblemsResponse;
import com.pullup.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problem")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("/{problemId}")
    public ResponseEntity<Void> toggleProblemBookmark(@PathVariable("problemId") Long problemId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        problemService.toggleProblemBookmark(problemId, memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/archive/all")
    public ResponseEntity<GetBookmarkedProblemsResponse> getBookmarkedProblems() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetBookmarkedProblemsResponse getBookmarkedProblemsResponse = problemService.getBookmarkedProblems(
                memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getBookmarkedProblemsResponse);
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<GetProblemResponse> getProblem(@PathVariable("problemId") Long problemId) {
        GetProblemResponse getProblemResponse = problemService.getProblem(problemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getProblemResponse);
    }

    @GetMapping("/wrong/recent")
    public ResponseEntity<GetRecentWrongProblemsResponse> getRecentWrongProblems() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetRecentWrongProblemsResponse getRecentWrongProblems = problemService.getRecentWrongProblems(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getRecentWrongProblems);

    }

    @GetMapping("/me/all")
    public ResponseEntity<GetAllWrongProblemsResponse> getAllWrongProblems() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetAllWrongProblemsResponse getAllWrongProblemsResponse = problemService.getAllWrongProblem(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getAllWrongProblemsResponse);
    }
}
