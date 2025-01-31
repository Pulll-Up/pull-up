package com.pullup.interview.repository;

import com.pullup.interview.domain.InterviewHint;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InterviewHintRepository extends CrudRepository<InterviewHint, Long> {
    List<InterviewHint> findByInterviewId(Long interviewId);
}
