package com.pullup.problem.repository;

import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.Subject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends CrudRepository<Problem, Long> {

    @Query("SELECT p "
            + "FROM Problem p "
            + "WHERE p.subject = :subject "
            + "AND p.correctRate >= :lowCorrectRate "
            + "AND p.correctRate <= :highCorrectRate "
            + "ORDER BY FUNCTION('RAND')")
    List<Problem> findTopNBySubjectAndCorrectRateOrderByRandom(
            @Param("subject") Subject subject,
            @Param("highCorrectRate") Integer highCorrectRate,
            @Param("lowCorrectRate") Integer lowCorrectRate,
            Pageable pageable
    );

    @Query("SELECT COUNT(p) "
            + "FROM Problem p "
            + "WHERE p.subject = :subject "
            + "AND p.correctRate >= :lowCorrectRate "
            + "AND p.correctRate <= :highCorrectRate")
    long countBySubjectAndCorrectRateBetween(
            @Param("subject") Subject subject,
            @Param("lowCorrectRate") Integer lowCorrectRate,
            @Param("highCorrectRate") Integer highCorrectRate
    );

    @Query(value = "SELECT * "
            + "FROM problem "
            + "WHERE subject = :subject "
            + "AND problem_type = 'SHORT_ANSWER' "
            + "AND CHAR_LENGTH(question) <= 30 "
            + "AND CHAR_LENGTH(answer) <= 30 "
            + "ORDER BY RAND() "
            + "LIMIT :limit",
            nativeQuery = true)
    List<Problem> findRandomShortAnswerProblemsBySubject(@Param("subject") String subject, @Param("limit") int limit);

    @Query(value = "SELECT * "
            + "FROM problem "
            + "WHERE problem_type = 'SHORT_ANSWER' "
            + "AND CHAR_LENGTH(question) <= 30 "
            + "AND CHAR_LENGTH(answer) <= 30 "
            + "ORDER BY RAND() "
            + "LIMIT :limit",
            nativeQuery = true)
    List<Problem> findRandomShortAnswerProblems(@Param("limit") int limit);


}
