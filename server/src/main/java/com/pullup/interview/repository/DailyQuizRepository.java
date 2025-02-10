package com.pullup.interview.repository;

import com.pullup.interview.domain.DailyQuiz;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DailyQuizRepository extends JpaRepository<DailyQuiz, Long> {

    @Query("SELECT dq.interviewId FROM DailyQuiz dq WHERE dq.memberId = :memberId")
    Optional<Long> findInterviewIdByMemberId(Long memberId);
}
