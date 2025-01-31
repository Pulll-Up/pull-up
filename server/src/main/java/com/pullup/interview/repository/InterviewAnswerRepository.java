package com.pullup.interview.repository;

import com.pullup.interview.domain.InterviewAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
    List<InterviewAnswer> findAllByMemberId(Long memberId);

    List<InterviewAnswer> findAllByInterviewId(Long interviewId);
}
