package com.pullup.exam.repository;

import com.pullup.exam.domain.ExamProblem;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ExamProblemRepository extends CrudRepository<ExamProblem, Long> {
    @Query("SELECT ep FROM ExamProblem ep " +
            "JOIN FETCH ep.exam e " +
            "JOIN FETCH ep.problem p " +
            "WHERE e.id = :examId")
    List<ExamProblem> findAllByExamId(@Param("examId") Long examId);

    @Query("SELECT ep FROM ExamProblem ep JOIN FETCH ep.problem WHERE ep.exam.id = :examId")
    List<ExamProblem> findByExamId(Long examId);

    List<ExamProblem> findTop10ByExamMemberIdAndAnswerStatusOrderByCreatedAtDesc(Long memberId, boolean answerStatus);

    @Query("SELECT ep, p, e, m " +
            "FROM ExamProblem ep " +
            "JOIN FETCH ep.problem p " +
            "JOIN FETCH ep.exam e " +
            "JOIN FETCH e.member m " +
            "WHERE m.id = :memberId " +
            "AND ep.answerStatus = false " +
            "ORDER BY ep.createdAt DESC")
    List<ExamProblem> findByExamMemberIdAndAnswerStatusFalseOrderByCreatedAtDesc(@Param("memberId") Long memberId);


    @Query("SELECT ep FROM ExamProblem ep " +
            "JOIN FETCH ep.problem p " +
            "JOIN FETCH ep.exam e " +
            "JOIN FETCH e.member m " +
            "WHERE ep.answerStatus = false " +
            "AND m.id = :memberId " +
            "AND (:title IS NULL OR p.question LIKE %:title%)" +
            "ORDER BY ep.createdAt DESC")
    List<ExamProblem> searchByMemberIdAndAnswerStatusFalseOrderByCreatedAtDesc(
            @Param("memberId") Long memberId,
            @Param("title") String title
    );

}