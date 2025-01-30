package com.pullup.exam.repository;

import com.pullup.exam.domain.ExamProblem;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ExamProblemRepository extends CrudRepository<ExamProblem, Long> {
    List<ExamProblem> findAllByExamId(Long examId);

    List<ExamProblem> findTop10ByExamMemberIdAndAnswerStatusOrderByCreatedAtDesc(Long memberId, boolean answerStatus);
}