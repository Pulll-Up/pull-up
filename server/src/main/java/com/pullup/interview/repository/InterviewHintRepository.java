package com.pullup.interview.repository;

import com.pullup.interview.domain.InterviewHint;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InterviewHintRepository extends CrudRepository<InterviewHint, Long> {
    @Query("SELECT ih FROM InterviewHint ih WHERE ih.interview.id = :interviewId")
    List<InterviewHint> findByInterviewId(Long interviewId);
}