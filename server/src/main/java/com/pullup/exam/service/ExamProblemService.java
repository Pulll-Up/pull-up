package com.pullup.exam.service;

import com.pullup.exam.domain.Exam;
import com.pullup.exam.domain.ExamProblem;
import com.pullup.exam.repository.ExamProblemRepository;
import com.pullup.problem.domain.Problem;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamProblemService {
    private final ExamProblemRepository examProblemRepository;




}