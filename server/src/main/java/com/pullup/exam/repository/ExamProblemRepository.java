package com.pullup.exam.repository;

import com.pullup.exam.domain.ExamProblem;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExamProblemRepository extends CrudRepository<ExamProblem, Long> {
    List<ExamProblem> findAllByExamId(Long examId);

    @Query("SELECT ep FROM ExamProblem ep JOIN FETCH ep.problem WHERE ep.exam.id = :examId")
    List<ExamProblem> findByExamId(Long examId);
}