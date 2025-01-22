package com.pullup.problem.repository;

import com.pullup.problem.domain.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProblemRepository extends CrudRepository<Problem, Long> {

    @Query("SELECT p "
            + "FROM Problem p "
            + "WHERE p.subject = :subject "
            + "AND p.correctRate >= :lowCorrectRate "
            + "AND p.correctRate <= :highCorrectRate "
            + "ORDER BY FUNCTION('RAND')")
    List<Problem> findTopNBySubjectAndCorrectRateOrderByRandom(
            @Param("subject") String subject,
            @Param("highCorrectRate") Integer highCorrectRate,
            @Param("lowCorrectRate") Integer lowCorrectRate,
            Pageable pageable
    );
}
