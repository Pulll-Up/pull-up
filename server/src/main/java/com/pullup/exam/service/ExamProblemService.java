package com.pullup.exam.service;

import com.pullup.exam.repository.ExamProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamProblemService {
    private final ExamProblemRepository examProblemRepository;


}