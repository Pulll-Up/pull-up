package com.pullup.interview.repository;

import com.pullup.interview.domain.Interview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findInterviewById(Long interviewId);

    @Query("SELECT i FROM Interview i " +
            "WHERE i.subject IN (SELECT s.subject FROM InterestSubject s WHERE s.member.id = :memberId) " +
            "AND NOT EXISTS (SELECT 1 FROM InterviewAnswer ia WHERE ia.interview.id = i.id AND ia.member.id = :memberId) "
            +
            "ORDER BY FUNCTION('RAND') LIMIT 1")
    Optional<Interview> findUnansweredInterviewByRandomAndSubject(@Param("memberId") Long memberId);

    @Query("SELECT i FROM Interview i " +
            "WHERE NOT EXISTS (SELECT 1 FROM InterviewAnswer ia WHERE ia.interview.id = i.id AND ia.member.id = :memberId) "
            +
            "ORDER BY FUNCTION('RAND') LIMIT 1")
    Optional<Interview> findUnansweredInterviewByRandom(@Param("memberId") Long memberId);

    List<Interview> findByQuestionContaining(@Param("keyword") String keyword);
}