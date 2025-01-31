package com.pullup.interview.repository;

import com.pullup.interview.domain.Interview;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findInterviewById(Long interviewId);
}