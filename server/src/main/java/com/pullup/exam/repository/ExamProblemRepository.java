package com.pullup.exam.repository;

import com.pullup.exam.domain.ExamProblem;
import org.springframework.data.repository.CrudRepository;

public interface ExamProblemRepository extends CrudRepository<ExamProblem, Long> {
}