package com.pullup.interview.repository;

import com.pullup.interview.domain.InterviewAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview " +
            "WHERE ia.member.id = :memberId")
    List<InterviewAnswer> findAllByMemberIdAndInterview(Long memberId);

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview i " +
            "JOIN FETCH ia.member m " +
            "WHERE ia.interview.id = :interviewId")
    List<InterviewAnswer> findAllByInterviewId(@Param("interviewId") Long interviewId);
}