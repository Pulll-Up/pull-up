package com.pullup.exam.repository;

import com.pullup.exam.domain.ExamProblem;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ExamProblemRepository extends CrudRepository<ExamProblem, Long> {
    List<ExamProblem> findAllByExamId(Long examId);

    List<ExamProblem> findTop10ByExamMemberIdAndAnswerStatusOrderByCreatedAtDesc(Long memberId, boolean answerStatus);

    @Query("SELECT ep, p, e, m " +
            "FROM ExamProblem ep " +
            "JOIN FETCH ep.problem p " +
            "JOIN FETCH ep.exam e " +
            "JOIN FETCH e.member m " +
            "WHERE m.id = :memberId " +
            "AND ep.answerStatus = false")
    List<ExamProblem> findByExamMemberIdAndAnswerStatusFalse(@Param("memberId") Long memberId);
}